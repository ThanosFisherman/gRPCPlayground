package com.thanosfisherman.grpcplayground.domain.states


sealed class LoginResultState<out T : Any> {

    object Idle : LoginResultState<Nothing>()

    object Loading : LoginResultState<Nothing>()

    object WrongEmail : LoginResultState<Nothing>()

    object LoggedOut : LoginResultState<Nothing>()

    object NoInternet : LoginResultState<Nothing>()

    data class Success<T : Any>(val data: T) : LoginResultState<T>()

    data class Error(val error: Exception? = null) : LoginResultState<Nothing>()

}
