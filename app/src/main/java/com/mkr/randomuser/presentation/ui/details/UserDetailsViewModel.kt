package com.mkr.randomuser.presentation.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mkr.randomuser.core.common.Resource
import com.mkr.randomuser.core.error.ErrorContext
import com.mkr.randomuser.core.error.ErrorHandler
import com.mkr.randomuser.domain.model.User
import com.mkr.randomuser.domain.usecase.GetUserByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val errorHandler: ErrorHandler,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: Int = checkNotNull(savedStateHandle["userId"])

    private val _uiState = MutableStateFlow(UserDetailsUiState())
    val uiState: StateFlow<UserDetailsUiState> = _uiState.asStateFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch(errorHandler.exceptionHandler) {
            _uiState.update { it.copy(isLoading = true) }
            try {
                when (val result = getUserByIdUseCase(userId)) {
                    is Resource.Success -> _uiState.update {
                        it.copy(isLoading = false, user = result.data)
                    }
                    is Resource.Error -> {
                        val errorMessage = result.message.ifBlank { ErrorContext.LOADING_USER_DETAILS }
                        errorHandler.handleError(errorMessage)
                        _uiState.update { it.copy(isLoading = false) }
                    }
                    Resource.Loading -> Unit
                }
            } catch (e: Exception) {
                errorHandler.handleError(e, ErrorContext.LOADING_USER_DETAILS)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}

data class UserDetailsUiState(
    val user: User? = null,
    val isLoading: Boolean = true
)