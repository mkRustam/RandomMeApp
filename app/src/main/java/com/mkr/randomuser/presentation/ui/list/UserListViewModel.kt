package com.mkr.randomuser.presentation.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mkr.randomuser.domain.model.User
import com.mkr.randomuser.domain.usecase.GetSavedUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getSavedUsersUseCase: GetSavedUsersUseCase
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    init {
        getSavedUsers()
    }

    private fun getSavedUsers() {
        viewModelScope.launch {
            try {
                val users = getSavedUsersUseCase()
                _users.value = users
            } catch (e: Exception) {
                // handle error
            }
        }
    }
}