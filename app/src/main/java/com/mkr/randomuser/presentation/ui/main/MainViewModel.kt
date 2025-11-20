package com.mkr.randomuser.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mkr.randomuser.core.common.Resource
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
    private val getRandomUserUseCase: GetRandomUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<MainEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onGenerateUserClicked(gender: String, nat: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                when (val result = getRandomUserUseCase(gender, nat)) {
                    is Resource.Success -> _events.send(MainEvent.NavigateToUserList)
                    is Resource.Error -> _uiState.update {
                        it.copy(errorMessage = result.message.ifBlank { "Something went wrong" })
                    }
                    Resource.Loading -> Unit
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun consumeError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

data class MainUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface MainEvent {
    data object NavigateToUserList : MainEvent
}