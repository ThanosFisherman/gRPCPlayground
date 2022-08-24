package com.thanosfisherman.grpcplayground.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thanosfisherman.grpcplayground.domain.usecases.GetCredentialsUC
import com.thanosfisherman.grpcplayground.domain.usecases.GetUserFromPrefsUseCase
import com.thanosfisherman.grpcplayground.domain.usecases.LogoutUseCase

//I don't like the fact that I pass two different instances of the same use case GetCredentialsUC but it works for what I want to achieve
class CredentialsViewModel(
    private val getCredentialsUC: GetCredentialsUC,
    private val getCredentialsFromCacheUC: GetCredentialsUC,
    private val getUserFromPrefsUseCase: GetUserFromPrefsUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    val userProfileState = getUserFromPrefsUseCase.stateResult
    val uiState = getCredentialsUC.stateResult
    val cachedCredentialsState = getCredentialsFromCacheUC.stateResult

    fun getCredentials() {
        getCredentialsUC(viewModelScope, false)
    }

    fun getCredentialsFromCache() {
        getCredentialsFromCacheUC(viewModelScope, true)
    }

    fun checkIfUserIsRegistered() {
        getUserFromPrefsUseCase(viewModelScope)
    }

    fun logout() {
        logoutUseCase(viewModelScope)
    }
}
