package com.thanosfisherman.grpcplayground.presentation.di


import com.thanosfisherman.grpcplayground.presentation.viewmodels.CredentialsViewModel
import com.thanosfisherman.grpcplayground.presentation.viewmodels.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { CredentialsViewModel(get(), get(), get(), get()) }
    viewModel { LoginViewModel(get()) }
}