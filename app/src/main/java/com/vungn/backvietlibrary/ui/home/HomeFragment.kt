package com.vungn.backvietlibrary.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.UncontainedCarouselStrategy
import com.vungn.backvietlibrary.R
import com.vungn.backvietlibrary.databinding.FragmentHomeBinding
import com.vungn.backvietlibrary.model.data.Book
import com.vungn.backvietlibrary.ui.activity.auth.AuthActivity
import com.vungn.backvietlibrary.ui.activity.book.BookActivity
import com.vungn.backvietlibrary.ui.activity.search.SearchActivity
import com.vungn.backvietlibrary.ui.home.adapter.BookCategoryAdapter
import com.vungn.backvietlibrary.ui.home.adapter.CarouselBookCategoryAdapter
import com.vungn.backvietlibrary.ui.home.adapter.ViewPagerAdapter
import com.vungn.backvietlibrary.util.GridItemDecoration
import com.vungn.backvietlibrary.util.books
import com.vungn.backvietlibrary.util.listener.OnItemClick
import kotlin.math.abs

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupViewPager()
        setupBookCategoryFirstTime()
        setupBookCategoryForYou()
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