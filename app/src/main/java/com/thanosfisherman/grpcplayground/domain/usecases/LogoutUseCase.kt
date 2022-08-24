package com.thanosfisherman.grpcplayground.domain.usecases

import com.thanosfisherman.grpcplayground.domain.repos.CredentialsRepo
import com.thanosfisherman.grpcplayground.domain.repos.PrefsRepo
import kotlinx.coroutines.CoroutineScope


class LogoutUseCase(
    private val prefsRepo: PrefsRepo,
    private val credentialsRepo: CredentialsRepo
) :
    BaseUseCase<Unit, Boolean>() {
    override suspend fun execute(scope: CoroutineScope, vararg params: Unit) {
        prefsRepo.deleteUserProfile()
        credentialsRepo.deleteCredentials()
        stateResultMutable.value = true
    }

    override val initialState: Boolean
        get() = false
}