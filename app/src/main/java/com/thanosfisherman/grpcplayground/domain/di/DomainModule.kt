package com.thanosfisherman.grpcplayground.domain.di

import com.thanosfisherman.grpcplayground.domain.usecases.GetCredentialsUC
import com.thanosfisherman.grpcplayground.domain.usecases.GetUserFromPrefsUseCase
import com.thanosfisherman.grpcplayground.domain.usecases.LoginUseCase
import com.thanosfisherman.grpcplayground.domain.usecases.LogoutUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetCredentialsUC(get()) }
    factory { GetUserFromPrefsUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { LogoutUseCase(get(), get()) }
}
