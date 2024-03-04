package com.vungn.backvietlibrary.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.UncontainedCarouselStrategy
import com.vungn.backvietlibrary.databinding.FragmentHomeBinding
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.ui.activity.book.BookActivity
import com.vungn.backvietlibrary.ui.activity.main.MainActivity
import com.vungn.backvietlibrary.ui.base.FragmentBase
import com.vungn.backvietlibrary.ui.home.adapter.BookCategoryAdapter
import com.vungn.backvietlibrary.ui.home.adapter.CarouselBookCategoryAdapter
import com.vungn.backvietlibrary.ui.home.adapter.ViewPagerAdapter
import com.vungn.backvietlibrary.ui.home.contract.impl.HomeViewModelImpl
import com.vungn.backvietlibrary.util.GridItemDecoration
import com.vungn.backvietlibrary.util.listener.OnItemClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : FragmentBase<FragmentHomeBinding, HomeViewModelImpl>() {
    private lateinit var carouselBookCategoryAdapter: CarouselBookCategoryAdapter
    private lateinit var bookCategoryAdapter: BookCategoryAdapter
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun getViewModelClass(): Class<HomeViewModelImpl> = HomeViewModelImpl::class.java

    override fun setupListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.books.collect {
                    carouselBookCategoryAdapter.data = it
                    bookCategoryAdapter.data = it
                    viewPagerAdapter.data = it
                    carouselBookCategoryAdapter.notifyItemRangeChanged(0, it.size)
                    bookCategoryAdapter.notifyItemRangeChanged(0, it.size)
                    viewPagerAdapter.notifyItemRangeChanged(0, it.size)
                }
            }
        }
    }

    override fun setupViews() {
        (requireActivity() as MainActivity).setTopBarTitle("Thư viện sách")
        defineAdapters()
        setupViewPager()
//        setupBookCategoryFirstTime()
        setupBookCategoryForYou()
    }

    private fun defineAdapters() {
        carouselBookCategoryAdapter = CarouselBookCategoryAdapter(requireContext())
        bookCategoryAdapter = BookCategoryAdapter(requireContext())
        viewPagerAdapter = ViewPagerAdapter(requireContext(), binding.vp2)
    }

    @SuppressLint("RestrictedApi")
    private fun setupBookCategoryForYou() {
        val bookCategory = binding.bookCategoryForYou
        val adapter = carouselBookCategoryAdapter
        val layoutManager =
            CarouselLayoutManager(UncontainedCarouselStrategy(), CarouselLayoutManager.HORIZONTAL)
        val decoration = object : ItemDecoration() {}
        adapter.data = viewModel.books.value
        adapter.onItemClick = gotoBookEntity
        bookCategory.setRVHeight(1000)
        bookCategory.setAdapter(adapter)
        bookCategory.setDecoration(decoration)
        bookCategory.setLayoutManager(layoutManager)
    }

    private fun setupBookCategoryFirstTime() {
        val bookCategory = binding.bookCategoryFirstTime
        val adapter = bookCategoryAdapter
        val layoutManager = object : GridLayoutManager(requireContext(), 3) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val decoration = GridItemDecoration(50, 3, true)
        adapter.data = viewModel.books.value
        adapter.onItemClick = gotoBookEntity
        bookCategory.setAdapter(adapter)
        bookCategory.setDecoration(decoration)
        bookCategory.setLayoutManager(layoutManager)
    }

    private fun setupViewPager() {
        val pager = binding.vp2
        val adapter = viewPagerAdapter
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            val scale = 0.65f + r * 0.35f
            page.scaleY = scale
            page.scaleX = scale
        }
        adapter.data = viewModel.books.value
        adapter.onItemClick = gotoBookEntity
        pager.adapter = adapter
        pager.clipToPadding = false
        pager.clipChildren = false
        pager.offscreenPageLimit = 3
        pager.currentItem = viewModel.books.value.size / 2
        pager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        pager.setPageTransformer(compositePageTransformer)
    }

    private val gotoBookEntity = object : OnItemClick<BookEntity> {
        override fun onItemClick(value: BookEntity) {
            val intent = Intent(requireContext(), BookActivity::class.java)
            intent.putExtra(BookActivity.KEY_BUNDLE_BOOK, value)
            startActivity(intent)
        }
    }
}