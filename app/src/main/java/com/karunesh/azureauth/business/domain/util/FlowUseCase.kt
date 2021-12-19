package com.karunesh.azureauth.business.domain.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn


abstract class FlowUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {
    @ExperimentalCoroutinesApi
    suspend operator fun invoke(parameters: P): Flow<Result<R>> =
        execute(parameters)
            .catch { e -> emit(Result.Error(Exception(e))) }
            .flowOn(coroutineDispatcher)


    protected abstract suspend fun execute(parameters: P): Flow<Result<R>>
}
