package com.mkr.randomuser.presentation.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mkr.randomuser.core.error.ErrorContext
import com.mkr.randomuser.core.error.ErrorHandler
import com.mkr.randomuser.domain.model.User
import com.mkr.randomuser.domain.usecase.GetSavedUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getSavedUsersUseCase: GetSavedUsersUseCase,
    private val errorHandler: ErrorHandler
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserListUiState())
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()

    init {
        observeUsers()
    }

    private fun observeUsers() {
        viewModelScope.launch(errorHandler.exceptionHandler) {
            getSavedUsersUseCase()
                .catch { throwable ->
                    errorHandler.handleError(throwable, ErrorContext.LOADING_USERS)
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                }
                .collect { users ->
                    _uiState.update {
                        it.copy(
                            users = users,
                            isLoading = false,
                            isEmpty = users.isEmpty()
                        )
                    }
                }
        }
    }
}

data class UserListUiState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false
)