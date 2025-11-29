package com.mkr.randomuser.core.error

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandler @Inject constructor() {

    private val _errorEvents = Channel<ErrorEvent>(Channel.BUFFERED)
    val errorEvents = _errorEvents.receiveAsFlow()

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable.message ?: "An unexpected error occurred")
    }

    fun handleError(message: String) {
        _errorEvents.trySend(ErrorEvent(message))
    }

    fun handleError(throwable: Throwable, context: String? = null) {
        val message = context ?: (throwable.message ?: "An unexpected error occurred")
        _errorEvents.trySend(ErrorEvent(message))
    }
}

data class ErrorEvent(
    val message: String
)

