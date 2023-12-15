package com.vungn.backvietlibrary.ui.requestauth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vungn.backvietlibrary.databinding.FragmentRequestAuthBinding
import com.vungn.backvietlibrary.ui.activity.auth.AuthActivity
import com.vungn.backvietlibrary.ui.activity.book.BookActivity

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
        }
    }

}