package com.vungn.backvietlibrary.ui.library.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vungn.backvietlibrary.db.entity.CategoryEntity
import com.vungn.backvietlibrary.ui.library.LibraryCategoriesFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private var _categories: List<CategoryEntity> = emptyList()
    var categories: List<CategoryEntity>
        get() = _categories
        set(value) {
            _categories = value
        }

    override fun getItemCount(): Int = _categories.size

    override fun createFragment(position: Int): Fragment {
        return LibraryCategoriesFragment(_categories[position])
    }
}