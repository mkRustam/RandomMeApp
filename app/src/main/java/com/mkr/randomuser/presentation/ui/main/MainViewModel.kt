package com.mkr.randomuser.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mkr.randomuser.core.common.Resource
import com.mkr.randomuser.core.error.ErrorContext
import com.mkr.randomuser.core.error.ErrorHandler
import com.mkr.randomuser.domain.usecase.GetRandomUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRandomUserUseCase: GetRandomUserUseCase,
    private val errorHandler: ErrorHandler
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<MainEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onGenerateUserClicked(gender: String, nat: String) {
        viewModelScope.launch(errorHandler.exceptionHandler) {
            _uiState.update { it.copy(isLoading = true) }
            try {
                when (val result = getRandomUserUseCase(gender, nat)) {
                    is Resource.Success -> _events.send(MainEvent.NavigateToUserList)
                    is Resource.Error -> {
                        val errorMessage = result.message.ifBlank { ErrorContext.GENERATING_USER }
                        errorHandler.handleError(errorMessage)
                    }
                    Resource.Loading -> Unit
                }
            } catch (e: Exception) {
                errorHandler.handleError(e, ErrorContext.GENERATING_USER)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}

data class MainUiState(
    val isLoading: Boolean = false
)

sealed interface MainEvent {
    data object NavigateToUserList : MainEvent
}