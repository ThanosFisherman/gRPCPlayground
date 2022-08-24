package com.thanosfisherman.grpcplayground.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thanosfisherman.grpcplayground.domain.usecases.LoginUseCase

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    val loginState = loginUseCase.stateResult

    fun triggerLogin(username: String, email: String) {
        loginUseCase(viewModelScope, username, email)
    }
}