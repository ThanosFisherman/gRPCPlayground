package com.thanosfisherman.grpcplayground.domain.usecases

import com.thanosfisherman.grpcplayground.domain.exceptions.CredentialsException
import com.thanosfisherman.grpcplayground.domain.models.DCredential
import com.thanosfisherman.grpcplayground.domain.repos.CredentialsRepo
import com.thanosfisherman.grpcplayground.domain.states.EventWrapper
import com.thanosfisherman.grpcplayground.domain.states.ResultState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update

class GetCredentialsUC(private val credentialsRepo: CredentialsRepo) :
    BaseUseCase<Boolean?, EventWrapper<ResultState<List<DCredential>>>>() {

    override val initialState: EventWrapper<ResultState<List<DCredential>>>
        get() = EventWrapper(ResultState.Idle)

    override suspend fun execute(scope: CoroutineScope, vararg params: Boolean?) {
        stateResultMutable.value = EventWrapper(ResultState.Loading)

        val stateResult = try {
            val creds = credentialsRepo.getCredentials(params[0]!!)
            if (creds.isEmpty())
                ResultState.Error(CredentialsException())
            else
                ResultState.Success(creds)
        } catch (e: Exception) {
            e.printStackTrace()
            ResultState.Error(e)
        }
        stateResultMutable.update { EventWrapper(stateResult) }
    }
}
