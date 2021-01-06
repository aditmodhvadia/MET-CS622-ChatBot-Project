package com.fazemeright.library.api.result

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T : Any> safeApiCall(call: suspend CoroutineScope.() -> Result<T>): Result<T> {
    return try {
        withContext(Dispatchers.IO) {
            call()
        }
    } catch (e: Exception) {
        Result.Error(e)
    }
}