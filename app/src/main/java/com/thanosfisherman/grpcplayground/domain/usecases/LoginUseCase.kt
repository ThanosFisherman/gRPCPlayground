package com.thanosfisherman.grpcplayground.domain.usecases

import com.thanosfisherman.grpcplayground.domain.models.DUserProfileModel
import com.thanosfisherman.grpcplayground.domain.repos.PrefsRepo
import com.thanosfisherman.grpcplayground.domain.states.LoginResultState
import kotlinx.coroutines.CoroutineScope
import java.util.*


class LoginUseCase(private val prefsRepo: PrefsRepo) :
    BaseUseCase<String, LoginResultState<DUserProfileModel>>() {

    override val initialState: LoginResultState<DUserProfileModel>
        get() = LoginResultState.Idle

    override suspend fun execute(scope: CoroutineScope, vararg params: String) {
        when {
            params[0].isBlank() -> {
                stateResultMutable.value = LoginResultState.WrongEmail
            }
            !params[1].contains("@") -> {
                stateResultMutable.value = LoginResultState.WrongEmail
            }
            params[1].isBlank() -> {
                stateResultMutable.value = LoginResultState.WrongEmail
            }
            else -> {
                val userId = UUID.randomUUID().toString()
                val username = params[0]
                val email = params[1]
                val user = DUserProfileModel(userId, email, username)
                prefsRepo.storeUserProfile(user)
                stateResultMutable.value = LoginResultState.Success(user)
            }
        }
    }
}