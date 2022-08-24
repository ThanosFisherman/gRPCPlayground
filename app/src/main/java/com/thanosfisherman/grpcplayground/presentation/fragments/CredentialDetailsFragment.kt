package com.thanosfisherman.grpcplayground.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.thanosfisherman.grpcplayground.databinding.FragmentCredentialDetailsBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CredentialDetailsFragment : Fragment() {

    private var _binding: FragmentCredentialDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: CredentialDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCredentialDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val creds = args.credentialsParcelable
        with(binding.infoLayout) {
            titleInfo.text = creds.title
            idInfo.text = creds.id.toString()
            issuedOnInfo.text = creds.issuedOn.toString()
            issuerInfo.text = creds.issuer
            subjectInfo.text = creds.subject
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}