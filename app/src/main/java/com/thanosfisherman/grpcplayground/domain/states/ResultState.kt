package com.thanosfisherman.grpcplayground.domain.states

sealed class ResultState<out T : Any> {

    object Loading : ResultState<Nothing>()
    object Idle : ResultState<Nothing>()
    data class Success<T : Any>(val data: T, val cached: Boolean = false) : ResultState<T>()
    data class Error(val error: Exception? = null) : ResultState<Nothing>()
}
