package com.thanosfisherman.grpcplayground.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.thanosfisherman.grpcplayground.R
import com.thanosfisherman.grpcplayground.databinding.FragmentCredentialsBinding
import com.thanosfisherman.grpcplayground.domain.models.DCredential
import com.thanosfisherman.grpcplayground.domain.models.DUserProfileModel
import com.thanosfisherman.grpcplayground.domain.states.EventWrapper
import com.thanosfisherman.grpcplayground.domain.states.LoginResultState
import com.thanosfisherman.grpcplayground.domain.states.ResultState
import com.thanosfisherman.grpcplayground.presentation.adapters.CredentialsRecyclerAdapter
import com.thanosfisherman.grpcplayground.presentation.common.extensions.gone
import com.thanosfisherman.grpcplayground.presentation.common.extensions.visible
import com.thanosfisherman.grpcplayground.presentation.common.utils.RapidSnack
import com.thanosfisherman.grpcplayground.presentation.viewmodels.CredentialsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.navigation.koinNavGraphViewModel


class CredentialsFragment : Fragment() {

    private var _binding: FragmentCredentialsBinding? = null
    private val binding get() = _binding!!


    private val credentialsViewModel: CredentialsViewModel by koinNavGraphViewModel(R.id.nav_graph)
    private val credentialsRecyclerAdapter: CredentialsRecyclerAdapter by lazy {
        CredentialsRecyclerAdapter(
            lifecycleScope
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCredentialsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        credentialsViewModel.checkIfUserIsRegistered()
        credentialsViewModel.getCredentialsFromCache()
    }

    private fun handleLoginStateResult(state: LoginResultState<DUserProfileModel>) {
        when (state) {
            LoginResultState.LoggedOut -> {
                credentialsRecyclerAdapter.submitList(null)
                binding.textInstructions.visible()
            }
            else -> {}
        }
    }

    private fun handleUiState(state: EventWrapper<ResultState<List<DCredential>>>) {
        when (val peekState = state.peekContent()) {
            is ResultState.Loading -> {}
            is ResultState.Idle -> {}
            is ResultState.Success -> {
                binding.textInstructions.gone()
                credentialsRecyclerAdapter.submitList(peekState.data)
                state.getContentIfNotHandled()?.let {
                    RapidSnack.success(binding.fab)
                }
            }
            is ResultState.Error -> {
                state.getContentIfNotHandled()?.let {
                    RapidSnack.error(
                        binding.fab,
                        "ERROR " + peekState.error?.localizedMessage
                    )
                }
            }
        }
    }


    private fun handleCachedCredentialsState(state: EventWrapper<ResultState<List<DCredential>>>) {
        when (val peekState = state.peekContent()) {
            is ResultState.Success -> {
                binding.textInstructions.gone()
                credentialsRecyclerAdapter.submitList(peekState.data)
            }
            is ResultState.Error -> {
                credentialsRecyclerAdapter.submitList(null)
                binding.textInstructions.visible()
            }
            else -> {}
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        binding.fab.setOnClickListener { credentialsViewModel.getCredentials() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    credentialsViewModel.uiState.collect { state ->
                        handleUiState(state)
                    }
                }
                launch {
                    credentialsViewModel.cachedCredentialsState.collect { state ->
                        handleCachedCredentialsState(state)
                    }
                }
                launch {
                    credentialsRecyclerAdapter.itemClicks.collect { credentials ->
                        findNavController().navigate(
                            CredentialsFragmentDirections.actionCredentialsFragmentToCredentialDetailsFragment(
                                credentials
                            )
                        )
                    }
                }
                launch {
                    credentialsViewModel.userProfileState.collect {
                        handleLoginStateResult(it)
                    }
                }
            }
        }
    }


    private fun setupRecyclerView() {
        val layoutManagerVertical = LinearLayoutManager(requireContext())
        binding.recyclerCredentials.adapter = credentialsRecyclerAdapter
        binding.recyclerCredentials.layoutManager = layoutManagerVertical
        //binding.recyclerCredentials.addItemDecoration(MyDivider(requireContext()))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
