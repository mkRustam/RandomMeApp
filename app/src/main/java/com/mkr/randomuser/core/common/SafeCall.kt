package com.mkr.randomuser.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext

suspend inline fun <T> safeCall(
    dispatcher: CoroutineDispatcher,
    crossinline block: suspend () -> T
): Resource<T> {
    return try {
        val result = withContext(dispatcher) { block() }
        Resource.Success(result)
    } catch (cancellationException: CancellationException) {
        throw cancellationException
    } catch (throwable: Throwable) {
        Resource.Error(throwable.message.orEmpty(), throwable)
    }
}

