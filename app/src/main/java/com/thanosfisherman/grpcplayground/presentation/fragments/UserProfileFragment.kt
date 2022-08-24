package com.thanosfisherman.grpcplayground.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.thanosfisherman.grpcplayground.R
import com.thanosfisherman.grpcplayground.databinding.FragmentUserProfileBinding
import com.thanosfisherman.grpcplayground.domain.models.DUserProfileModel
import com.thanosfisherman.grpcplayground.domain.states.LoginResultState
import com.thanosfisherman.grpcplayground.presentation.activities.LoginActivity
import com.thanosfisherman.grpcplayground.presentation.common.extensions.setOnDebouncedClickListener
import com.thanosfisherman.grpcplayground.presentation.common.extensions.startActivity
import com.thanosfisherman.grpcplayground.presentation.common.extensions.switchAnimatorView
import com.thanosfisherman.grpcplayground.presentation.viewmodels.CredentialsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.navigation.koinNavGraphViewModel
import timber.log.Timber

class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private val REGISTER_VIEW = 1
    private val USER_VIEW = 2

    private val credentialsViewModel: CredentialsViewModel by koinNavGraphViewModel(R.id.nav_graph)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        credentialsViewModel.checkIfUserIsRegistered()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.registerLayout.registerButton.setOnDebouncedClickListener {
            activity?.startActivity<LoginActivity>()
        }
        binding.userLayout.logoutButton.setOnClickListener { credentialsViewModel.logout() }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    credentialsViewModel.userProfileState.collect {
                        Timber.i("USER PROFILE " + it)
                        handleLoginStateResult(it)
                    }
                }
            }
        }
    }

    private fun handleLoginStateResult(state: LoginResultState<DUserProfileModel>) {
        when (state) {
            is LoginResultState.Error -> {
                binding.animatorProfile.switchAnimatorView(REGISTER_VIEW)
            }
            is LoginResultState.Success -> {
                binding.userLayout.txtWelcome.text = getString(
                    R.string.welcome,
                    state.data.username
                )
                binding.userLayout.txtEmail.text = state.data.email
                binding.userLayout.txtUuid.text = state.data.userId
                binding.animatorProfile.switchAnimatorView(USER_VIEW)
            }
            LoginResultState.Idle -> {}
            LoginResultState.Loading -> {}
            LoginResultState.LoggedOut -> {
                Timber.i("LOGGED OUT")
                binding.animatorProfile.switchAnimatorView(REGISTER_VIEW)
            }
            else -> {
                binding.animatorProfile.switchAnimatorView(REGISTER_VIEW)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}