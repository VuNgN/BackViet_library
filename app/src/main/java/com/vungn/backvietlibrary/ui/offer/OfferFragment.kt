package com.vungn.backvietlibrary.ui.offer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vungn.backvietlibrary.R
import com.vungn.backvietlibrary.databinding.FragmentOfferBinding
import com.vungn.backvietlibrary.ui.activity.auth.AuthActivity
import com.vungn.backvietlibrary.ui.activity.search.SearchActivity

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
    }

    private fun setupListener() {
        binding.apply {
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.account -> {
                        val intent = Intent(requireContext(), AuthActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.item_search -> {
                        val intent = Intent(requireContext(), SearchActivity::class.java)
                        startActivity(intent)
                    }

                    else -> {}
                }
                true
            }
        }
    }
}