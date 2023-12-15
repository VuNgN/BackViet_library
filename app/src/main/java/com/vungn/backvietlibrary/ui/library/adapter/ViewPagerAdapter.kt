package com.vungn.backvietlibrary.ui.library.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vungn.backvietlibrary.ui.library.LibraryCategoriesFragment
import com.vungn.backvietlibrary.util.data.Category

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private var _size = 0
    var size: Int
        get() = _size
        set(value) {
            _size = value
        }

    override fun getItemCount(): Int = _size

    override fun createFragment(position: Int): Fragment {
        return LibraryCategoriesFragment(Category.entries[position])
    }
}