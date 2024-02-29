package com.vungn.backvietlibrary.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.vungn.backvietlibrary.databinding.FragmentLibraryBinding
import com.vungn.backvietlibrary.ui.activity.main.MainActivity
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

    private fun setupListener() {}

    private fun setupUi() {
        (requireActivity() as MainActivity).apply {
            setTopBarTitle("Library")
            setupTabLayout(setupListener = ::setupTabListener)
        }
        val adapter =
            ViewPagerAdapter(requireActivity().supportFragmentManager, requireActivity().lifecycle)
        adapter.size = Category.entries.size
        binding.viewPager2.adapter = adapter
    }

    private fun setupTabListener(tabLayout: TabLayout) {
        binding.apply {
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
                    tabLayout.selectTab(tabLayout.getTabAt(position))
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).showTabLayout()
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as MainActivity).hideTabLayout()
    }
}