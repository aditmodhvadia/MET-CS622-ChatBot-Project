package com.fazemeright.library.api.result

interface TaskResult<T> {
    val isSuccessful: Boolean
    val isFailed: Boolean
    val data: T
    val exception: Exception?
}