package com.vungn.backvietlibrary.ui.library

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.vungn.backvietlibrary.databinding.FragmentLibraryBinding
import com.vungn.backvietlibrary.ui.activity.main.MainActivity
import com.vungn.backvietlibrary.ui.base.FragmentBase
import com.vungn.backvietlibrary.ui.library.adapter.ViewPagerAdapter
import com.vungn.backvietlibrary.ui.library.contract.impl.LibraryViewModelImpl
import kotlinx.coroutines.launch

class LibraryFragment : FragmentBase<FragmentLibraryBinding, LibraryViewModelImpl>() {
    override fun getViewBinding(): FragmentLibraryBinding {
        return FragmentLibraryBinding.inflate(layoutInflater)
    }

    override fun getViewModelClass(): Class<LibraryViewModelImpl> {
        return LibraryViewModelImpl::class.java
    }

    override fun setupListener() {}
    override fun setupViews() {
        (requireActivity() as MainActivity).apply {
            setTopBarTitle("Library")
            setupTabLayoutListener(setupListener = ::setupTabListener)
        }
        val adapter =
            ViewPagerAdapter(requireActivity().supportFragmentManager, requireActivity().lifecycle)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.categories.collect { categories ->
                    adapter.categories = categories
                    binding.viewPager2.adapter = adapter
                }
            }
        }
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