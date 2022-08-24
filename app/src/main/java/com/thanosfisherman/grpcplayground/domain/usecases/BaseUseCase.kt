package com.thanosfisherman.grpcplayground.domain.usecases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


abstract class BaseUseCase<Params, State> {

    private var job: Job? = null
    protected abstract val initialState: State
    protected val stateResultMutable: MutableStateFlow<State> by lazy {
        MutableStateFlow(
            initialState
        )
    }
    val stateResult: StateFlow<State> by lazy { stateResultMutable.asStateFlow() }

    abstract suspend fun execute(scope: CoroutineScope, vararg params: Params)

    operator fun invoke(scope: CoroutineScope, vararg params: Params) {
        job?.cancel()
        job = scope.launch(Dispatchers.IO) { execute(scope, *params) }
    }
}
