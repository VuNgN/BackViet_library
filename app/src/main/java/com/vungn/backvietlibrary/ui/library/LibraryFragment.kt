package com.vungn.backvietlibrary.ui.library

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.vungn.backvietlibrary.R
import com.vungn.backvietlibrary.databinding.FragmentLibraryBinding
import com.vungn.backvietlibrary.ui.activity.auth.AuthActivity
import com.vungn.backvietlibrary.ui.activity.search.SearchActivity
import com.vungn.backvietlibrary.ui.library.adapter.ViewPagerAdapter
import com.vungn.backvietlibrary.util.data.Category

class LibraryFragment : Fragment() {
    private lateinit var binding: FragmentLibraryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setupListener()
    }

    private fun setupUi() {
        val adapter =
            ViewPagerAdapter(requireActivity().supportFragmentManager, requireActivity().lifecycle)
        adapter.size = Category.entries.size
        binding.viewPager2.adapter = adapter
        repeat(Category.entries.size) {
            binding.tabLayout.addTab(
                binding.tabLayout.newTab().setIcon(Category.entries[it].resource)
                    .setText(Category.entries[it].title)
            )
        }
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
            tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(p0: TabLayout.Tab?) {
                    if (p0 != null) {
                        binding.viewPager2.currentItem = p0.position
                    }
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {}

                override fun onTabReselected(p0: TabLayout.Tab?) {}
            })
            viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
                }
            })
        }
    }
}