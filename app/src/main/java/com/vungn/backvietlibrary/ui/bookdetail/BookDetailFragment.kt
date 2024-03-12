package com.vungn.backvietlibrary.ui.bookdetail

import android.app.ActionBar
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout.LayoutParams
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.setPadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.slider.Slider
import com.vungn.backvietlibrary.R
import com.vungn.backvietlibrary.databinding.FragmentBookDetailBinding
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.ui.base.FragmentBase
import com.vungn.backvietlibrary.ui.bookdetail.adapter.ViewPagerAdapter
import com.vungn.backvietlibrary.ui.bookdetail.contract.impl.BookDetailViewModelImpl
import com.vungn.backvietlibrary.ui.component.BookDetailLoadingDialogFragment
import com.vungn.backvietlibrary.util.data.Page
import com.vungn.backvietlibrary.util.enums.CallApiState
import com.vungn.backvietlibrary.util.enums.LoadFileState
import com.vungn.backvietlibrary.util.extension.getColorAttr
import com.vungn.backvietlibrary.util.listener.OnImageZoom
import com.vungn.backvietlibrary.util.listener.OnItemClick
import com.vungn.backvietlibrary.util.state.PageUpdateState
import com.vungn.backvietlibrary.util.transformer.BookFlipPageTransformer2
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.math.round


class BookDetailFragment : FragmentBase<FragmentBookDetailBinding, BookDetailViewModelImpl>() {
    private val args: BookDetailFragmentArgs by navArgs()
    private lateinit var book: BookEntity
    private lateinit var dialog: BookDetailLoadingDialogFragment

    private lateinit var adapter: ViewPagerAdapter
    private var isSliding: Boolean = false
    private val screenSize: Point = Point()
    private var mVisible = true
    private val mHideHandler = Handler()
    private lateinit var mContentView: View
    private val UI_ANIMATION_DELAY = 300
    private val mHidePart2Runnable = Runnable { // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        mContentView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    private val mShowPart2Runnable = Runnable { // Delayed display of UI elements
        requireActivity().actionBar?.show()
        updateUI(false)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun show() {
        // Show the system bar
        mContentView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun hide() {
        // Hide UI first
        val actionBar: ActionBar? = requireActivity().actionBar
        actionBar?.hide()
        updateUI(true)
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun fullScreenSetup() {
        val window = requireActivity().window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars.
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT

        // Add a listener to update the behavior of the toggle fullscreen button when
        // the system bars are hidden or revealed.
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            // You can hide the caption bar even when the other system bars are visible.
            // To account for this, explicitly check the visibility of navigationBars()
            // and statusBars() rather than checking the visibility of systemBars().
            adapter.onItemClick = object : OnItemClick<Page> {
                override fun onItemClick(value: Page) {
                    viewModel.updatePage(value)
                    if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars()) || windowInsets.isVisible(
                            WindowInsetsCompat.Type.statusBars()
                        )
                    ) {
                        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
//                        requireActivity().requestWindowFeature(Window.FEATURE_NO_TITLE)
                        updateUI(true)
                    } else {
                        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
                        updateUI(false)
                    }
                }
            }

            view.onApplyWindowInsets(windowInsets)
        }
    }


    override fun onStart() {
        super.onStart()
        book = args.book
        try {
            lifecycleScope.launch {
                viewModel.updateState.collect {
                    Log.d("", "onStart: ${it.name}")
                }
            }
            if (viewModel.updateState.value != PageUpdateState.DONE) {
                try {
                    viewModel.openRenderer(
                        requireContext(), book.id, book.pageNumber?.toInt()?.minus(1) ?: 0
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowManager: WindowManager =
            requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getRealSize(screenSize)
        requireActivity().supportFragmentManager.executePendingTransactions()
        dialog = BookDetailLoadingDialogFragment()
        dialog.isCancelable = false
    }

    override fun onStop() {
        super.onStop()
        viewModel.closeRenderer()
    }

    override fun getViewBinding(): FragmentBookDetailBinding {
        return FragmentBookDetailBinding.inflate(layoutInflater)
    }

    override fun getViewModelClass(): Class<BookDetailViewModelImpl> {
        return BookDetailViewModelImpl::class.java
    }

    override fun setupListener() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val currentPage = adapter.currentList[position]
                viewModel.updatePage(currentPage)
                if (!isSliding) {
                    binding.slider.value =
                        (currentPage.page.toFloat() / viewModel.pagesCount.value) * 100
                }
                binding.pageNumber.text = getString(
                    R.string.page_number,
                    currentPage.page.toString(),
                    viewModel.pagesCount.value.toString()
                )
            }
        })
        binding.slider.addOnChangeListener { slider, value, fromUser ->
            if (fromUser) {
                val gotoPage = round(value / 100 * viewModel.pagesCount.value).toInt()
                binding.viewpager.currentItem = gotoPage
                binding.viewpager.computeScroll()
            }
        }
        binding.slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(p0: Slider) {
                isSliding = true
            }

            override fun onStopTrackingTouch(p0: Slider) {
                isSliding = false
            }

        })
    }

    private fun updateUI(isFullScreen: Boolean) {
        if (isFullScreen) {
            hideViews()
        } else {
            showViews()
        }
    }

    private fun hideViews() {
        binding.topAppBar.visibility = View.GONE
        binding.pageInfo.visibility = View.GONE
        binding.viewpager.visibility = View.GONE
        binding.viewpagerFullScreen.visibility = View.VISIBLE
        adapter.isZoomable = true
        binding.viewpagerFullScreen.setCurrentItem(
            viewModel.currentPage.value?.page?.minus(1) ?: 0, false
        )
    }

    private fun showViews() {
        binding.topAppBar.visibility = View.VISIBLE
        binding.pageInfo.visibility = View.VISIBLE
        binding.viewpager.visibility = View.VISIBLE
        binding.viewpagerFullScreen.visibility = View.GONE
        adapter.isZoomable = false
        binding.viewpager.setCurrentItem(viewModel.currentPage.value?.page?.minus(1) ?: 0, false)
        binding.viewpager.computeScroll()
    }

    override fun setupViews() {
        requireActivity().window.statusBarColor =
            requireContext().getColorAttr(android.R.attr.colorBackground)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            fullScreenSetup()
        } else {
            adapter.onItemClick = object : OnItemClick<Page> {
                override fun onItemClick(value: Page) {
                    viewModel.updatePage(value)
                    toggle()
                }
            }
        }
        adapter = ViewPagerAdapter(this.requireContext())

        setupPagerFullScreen()
        setupPager()
        setupDialog()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pages.collect {
                    Log.d("", "setupViews: ${it.size}")
                    adapter.submitList(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pagesCount.collect {
                    val current = 1
                    binding.pageNumber.text = getString(
                        R.string.page_number, current.toString(), it.toString()
                    )
                }
            }
        }
        updateUI(false)
    }

    private fun setupDialog() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.callApiState.collect {
                    when (it) {
                        CallApiState.LOADING -> {
                            if (!dialog.isAdded) {
                                dialog.show(requireActivity().supportFragmentManager, TAG)
                            }
                        }

                        CallApiState.SUCCESS -> {
                            if (dialog.isAdded) {
                                dialog.dismiss()
                            }
                        }

                        CallApiState.ERROR -> {
                            Toast.makeText(
                                requireContext(),
                                "Có lỗi xảy ra trong khi lấy dữ liệu. Vui lòng thử lại.",
                                Toast.LENGTH_LONG
                            ).show()
                            navController.popBackStack()
                        }

                        CallApiState.NONE -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadBookState.collect {
                    when (it) {
                        LoadFileState.LOADING -> {
                            if (!dialog.isAdded) {
                                dialog.show(requireActivity().supportFragmentManager, TAG)
                            }
                        }

                        LoadFileState.SUCCESS -> {
                            if (dialog.isAdded) {
                                dialog.dismiss()
                            }
                        }

                        LoadFileState.ERROR -> {
                            Toast.makeText(
                                requireContext(),
                                "Có lỗi xảy ra trong khi đọc dữ liệu. Vui lòng thử lại.",
                                Toast.LENGTH_LONG
                            ).show()
                            navController.popBackStack()
                        }

                        LoadFileState.NONE -> {}
                    }
                }
            }
        }
    }

    private fun setupPager() {
        val pager = binding.viewpager
        pager.adapter = adapter
        pager.clipToPadding = false
        pager.clipChildren = false
        val targetWidth = screenSize.x * 2 / 3
        val targetPaddingVertical = 40
        val targetPaddingHorizontal = ((screenSize.x - targetWidth) / 2) + targetPaddingVertical
        val targetHeight = screenSize.y * 2 / 3
        pager.layoutParams.apply {
            height = targetHeight
        }
        pager.setPadding(
            targetPaddingHorizontal,
            targetPaddingVertical,
            targetPaddingHorizontal,
            targetPaddingVertical
        )
        pager.offscreenPageLimit = 3
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(targetPaddingHorizontal / 2))
        pager.setPageTransformer(compositePageTransformer)
    }

    private fun setupPagerFullScreen() {
        mContentView = binding.viewpagerFullScreen
        val pager = binding.viewpagerFullScreen
        adapter.onImageZoom = object : OnImageZoom {
            override fun onImageZoom(isZooming: Boolean) {
                pager.isUserInputEnabled = !isZooming
            }
        }
        pager.adapter = adapter
        pager.clipToPadding = false
        pager.clipChildren = false
        val bookFlipPageTransformer = BookFlipPageTransformer2()
        bookFlipPageTransformer.isEnableScale = true
        bookFlipPageTransformer.scaleAmountPercent = 10f
        pager.setPageTransformer(bookFlipPageTransformer)
        pager.setPadding(0)
        pager.offscreenPageLimit = 1
        pager.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    companion object {
        const val TAG = "BookDetailFragment"
    }
}