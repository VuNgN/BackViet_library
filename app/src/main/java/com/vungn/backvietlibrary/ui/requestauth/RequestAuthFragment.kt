package com.vungn.backvietlibrary.ui.requestauth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vungn.backvietlibrary.R
import com.vungn.backvietlibrary.databinding.FragmentRequestAuthBinding
import com.vungn.backvietlibrary.ui.activity.auth.AuthActivity

class RequestAuthFragment : Fragment() {
    private lateinit var binding: FragmentRequestAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRequestAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    private fun setupListener() {
        binding.apply {
            toolbar.apply {
                setNavigationOnClickListener {
                    requireActivity().finish()
                    (requireActivity() as AuthActivity).onBackPressedMethod()
                }
            }
            loginButton.setOnClickListener {
                findNavController().navigate(R.id.action_requestAuthFragment_to_loginFragment)
            }
        }
    }

}