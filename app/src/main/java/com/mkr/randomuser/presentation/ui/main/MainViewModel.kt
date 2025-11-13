package com.mkr.randomuser.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mkr.randomuser.domain.model.User
import com.mkr.randomuser.domain.usecase.GetRandomUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRandomUserUseCase: GetRandomUserUseCase
) : ViewModel() {

    private val _navigateToUserList = MutableSharedFlow<Unit>()
    val navigateToUserList = _navigateToUserList.asSharedFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun onGenerateUserClicked(gender: String, nat: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                getRandomUserUseCase(gender, nat)
                _navigateToUserList.emit(Unit)
            } catch (e: Exception) {
                _error.emit(e.message ?: "An unknown error occurred")
            } finally {
                _loading.value = false
            }
        }
    }
}