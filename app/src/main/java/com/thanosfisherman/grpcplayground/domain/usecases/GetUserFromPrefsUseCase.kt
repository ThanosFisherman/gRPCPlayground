package com.thanosfisherman.grpcplayground.domain.usecases

import com.thanosfisherman.grpcplayground.domain.models.DUserProfileModel
import com.thanosfisherman.grpcplayground.domain.repos.PrefsRepo
import com.thanosfisherman.grpcplayground.domain.states.LoginResultState
import com.thanosfisherman.grpcplayground.domain.states.ResultState
import kotlinx.coroutines.CoroutineScope

class GetUserFromPrefsUseCase(private val prefsRepo: PrefsRepo) :
    BaseUseCase<Unit, LoginResultState<DUserProfileModel>>() {
    override suspend fun execute(scope: CoroutineScope, vararg params: Unit) {
        prefsRepo.getUserProfile().collect {
            if (it.userId.isEmpty()) {
                stateResultMutable.value = LoginResultState.LoggedOut
                return@collect
            }
            stateResultMutable.value = LoginResultState.Success(it)
        }
    }

    override val initialState: LoginResultState<DUserProfileModel>
        get() = LoginResultState.Idle
}