package com.dboy.todocompose.utils

sealed class RequestState {
    object Idle : RequestState()
    object Success : RequestState()
    data class Error(val error: Throwable) : RequestState()
}
