package com.vungn.backvietlibrary.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.UncontainedCarouselStrategy
import com.vungn.backvietlibrary.R
import com.vungn.backvietlibrary.databinding.FragmentHomeBinding
import com.vungn.backvietlibrary.model.data.Book
import com.vungn.backvietlibrary.ui.activity.auth.AuthActivity
import com.vungn.backvietlibrary.ui.activity.book.BookActivity
import com.vungn.backvietlibrary.ui.activity.search.SearchActivity
import com.vungn.backvietlibrary.ui.base.FragmentBase
import com.vungn.backvietlibrary.ui.home.adapter.BookCategoryAdapter
import com.vungn.backvietlibrary.ui.home.adapter.CarouselBookCategoryAdapter
import com.vungn.backvietlibrary.ui.home.adapter.ViewPagerAdapter
import com.vungn.backvietlibrary.ui.home.contract.impl.HomeViewModelImpl
import com.vungn.backvietlibrary.util.GridItemDecoration
import com.vungn.backvietlibrary.util.books
import com.vungn.backvietlibrary.util.listener.OnItemClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : FragmentBase<FragmentHomeBinding, HomeViewModelImpl>() {
    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun getViewModelClass(): Class<HomeViewModelImpl> = HomeViewModelImpl::class.java

    private fun setupTopAppBar() {
        lifecycleScope.launch {
            viewModel.avatar.collect { url ->
                Glide.with(requireContext()).asDrawable().load(url).circleCrop()
                    .into(object :
                        CustomTarget<Drawable>() {
                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            binding.toolbar.menu.getItem(1).setIcon(resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            binding.toolbar.menu.getItem(1).setIcon(placeholder)
                        }
                    })
            }
        }
    }

    override fun setupListener() {
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

    override fun setupViews() {
        setupViewPager()
        setupBookCategoryFirstTime()
        setupBookCategoryForYou()
        setupTopAppBar()
    }

    @SuppressLint("RestrictedApi")
    private fun setupBookCategoryForYou() {
        val bookCategory = binding.bookCategoryForYou
        val adapter = CarouselBookCategoryAdapter(requireContext())
        val layoutManager =
            CarouselLayoutManager(UncontainedCarouselStrategy(), CarouselLayoutManager.HORIZONTAL)
        val decoration = object : ItemDecoration() {}
        adapter.data = books
        adapter.onItemClick = gotoBook
        bookCategory.setRVHeight(1000)
        bookCategory.setAdapter(adapter)
        bookCategory.setDecoration(decoration)
        bookCategory.setLayoutManager(layoutManager)
    }

    private fun setupBookCategoryFirstTime() {
        val bookCategory = binding.bookCategoryFirstTime
        val adapter = BookCategoryAdapter(requireContext())
        val layoutManager = object : GridLayoutManager(requireContext(), 3) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val decoration = GridItemDecoration(50, 3, true)
        adapter.data = books
        adapter.onItemClick = gotoBook
        bookCategory.setAdapter(adapter)
        bookCategory.setDecoration(decoration)
        bookCategory.setLayoutManager(layoutManager)
    }

    private fun setupViewPager() {
        val pager = binding.vp2
        val adapter = ViewPagerAdapter(this.requireContext(), pager)
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            val scale = 0.65f + r * 0.35f
            page.scaleY = scale
            page.scaleX = scale
        }
        adapter.data = books
        adapter.onItemClick = gotoBook
        pager.adapter = adapter
        pager.clipToPadding = false
        pager.clipChildren = false
        pager.offscreenPageLimit = 3
        pager.currentItem = books.size / 2
        pager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        pager.setPageTransformer(compositePageTransformer)
    }

    private val gotoBook = object : OnItemClick<Book> {
        override fun onItemClick(value: Book) {
            val intent = Intent(requireContext(), BookActivity::class.java)
            intent.putExtra(BookActivity.KEY_BUNDLE_BOOK, value)
            startActivity(intent)
        }
    }
}