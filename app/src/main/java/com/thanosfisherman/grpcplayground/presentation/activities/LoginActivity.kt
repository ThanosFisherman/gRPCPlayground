package com.thanosfisherman.grpcplayground.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.thanosfisherman.grpcplayground.R
import com.thanosfisherman.grpcplayground.databinding.ActivityLoginBinding
import com.thanosfisherman.grpcplayground.domain.models.DUserProfileModel
import com.thanosfisherman.grpcplayground.domain.states.LoginResultState
import com.thanosfisherman.grpcplayground.presentation.common.utils.RapidSnack
import com.thanosfisherman.grpcplayground.presentation.viewmodels.LoginViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModel()

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            loginViewModel.triggerLogin(
                binding.usernameEdit.text.toString(),
                binding.emailEdit.text.toString()
            )
        }

        binding.closeImage.setOnClickListener { finish() }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.loginState.collect {
                    handleLoginStateResult(it)
                }
            }
        }
    }

    private fun handleLoginStateResult(state: LoginResultState<DUserProfileModel>) {
        when (state) {
            is LoginResultState.Error -> {
                RapidSnack.error(binding.loginButton)
            }
            is LoginResultState.Loading -> {
                //loginButton.showProgress()
            }
            is LoginResultState.Success -> {
                finish()
            }
            is LoginResultState.WrongEmail -> {
                RapidSnack.error(binding.loginButton, R.string.check_email)
            }

            is LoginResultState.NoInternet -> {
                RapidSnack.error(binding.loginButton, R.string.no_network)
            }
            LoginResultState.Idle -> {}
            LoginResultState.LoggedOut -> {}
        }
    }
}