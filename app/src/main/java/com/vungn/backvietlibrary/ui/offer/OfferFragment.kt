package com.vungn.backvietlibrary.ui.offer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vungn.backvietlibrary.databinding.FragmentOfferBinding
import com.vungn.backvietlibrary.ui.activity.main.MainActivity

class OfferFragment : Fragment() {
    private lateinit var binding: FragmentOfferBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOfferBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupView()
    }

    private fun setupView() {
        (requireActivity() as MainActivity).setTopBarTitle("Offer")
    }

    private fun setupListener() {
        binding.apply {
        }
    }
}