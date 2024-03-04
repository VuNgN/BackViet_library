package com.vungn.backvietlibrary.ui.bookoverview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.appbar.AppBarLayout
import com.vungn.backvietlibrary.R
import com.vungn.backvietlibrary.databinding.FragmentBookOverviewBinding
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.ui.activity.book.BookActivity
import com.vungn.backvietlibrary.ui.activity.book.BookActivity.Companion.KEY_BUNDLE_BOOK
import com.vungn.backvietlibrary.util.Common
import com.vungn.backvietlibrary.util.extension.startAlphaAnimation
import com.vungn.backvietlibrary.util.extension.startBackgroundAnimation
import com.vungn.backvietlibrary.util.extension.themeColor
import kotlin.math.abs

class BookOverviewFragment : Fragment(), AppBarLayout.OnOffsetChangedListener {
    private lateinit var binding: FragmentBookOverviewBinding
    private var bookEntity: BookEntity? = null
    private var mIsTheMenuSmallVisible = false
    private var mIsTheMenuLargeVisible = true

    @ColorInt
    private var dominantColor: Int = Color.TRANSPARENT

    @ColorInt
    private var vibrantColor: Int = Color.TRANSPARENT

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBookOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = this.arguments
        bookEntity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle?.getParcelable(KEY_BUNDLE_BOOK, BookEntity::class.java)
        } else {
            bundle?.getParcelable(KEY_BUNDLE_BOOK)
        }
        setupUI()
        setupListener()
    }

    private fun setupListener() {
        binding.apply {
            readBookButton.setOnClickListener {
                findNavController().navigate(R.id.action_bookOverviewFragment_to_bookDetailFragment)
            }
            toolbar.apply {
                setNavigationOnClickListener {
                    requireActivity().finish()
                    (requireActivity() as BookActivity).onBackPressedMethod()
                }
            }
        }
    }

    private fun setupUI() {
        loadBookCover()
        binding.apply {
            appBarLayout.addOnOffsetChangedListener(this@BookOverviewFragment)
            titleLarge.text = bookEntity?.name
            titleSmall.text = bookEntity?.name
            description.text = bookEntity?.description
        }
    }

    private fun loadBookCover() {
        val windowManager: WindowManager =
            requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        windowManager.defaultDisplay.getRealSize(size)
        val width = size.x
        val targetWidth = width / 3
        val targetHeight = targetWidth / 3 * 4

        Glide.with(this).asBitmap().load(bookEntity?.coverImage ?: Common.defaultBookCover)
            .apply(RequestOptions().override(targetWidth, targetHeight)).centerCrop()
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.bookCover.setImageBitmap(resource)
//                    Palette.Builder(resource).generate {
//                        it?.let { palette ->
//                            dominantColor = palette.getDominantColor(Color.TRANSPARENT)
//                            vibrantColor = palette.getVibrantColor(Color.TRANSPARENT)
//                            val mutedColor = palette.getMutedColor(Color.TRANSPARENT)
//                            val gradient = GradientDrawable(
//                                GradientDrawable.Orientation.TOP_BOTTOM,
//                                intArrayOf(vibrantColor, dominantColor)
//                            )
//                            binding.root.setBackgroundColor(mutedColor)
////                            binding.readBookButton.setBackgroundColor(mutedColor)
//                            requireActivity().window.statusBarColor = dominantColor
//                        }
//                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
        requireActivity().window.statusBarColor =
            requireContext().themeColor(com.google.android.material.R.attr.colorPrimaryContainer)
    }


    override fun onOffsetChanged(p0: AppBarLayout?, p1: Int) {
        val maxScroll: Int = binding.appBarLayout.totalScrollRange
        val percentage = abs(p1).toFloat() / maxScroll.toFloat()

        handleAlphaOnTitle(percentage)
        handleToolbarTitleVisibility(percentage)
    }


    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheMenuSmallVisible) {
                binding.linearlayoutMenuSmall.visibility = VISIBLE
                binding.linearlayoutMenuSmall.startAlphaAnimation(
                    ALPHA_ANIMATIONS_DURATION, VISIBLE
                )
                binding.toolbar.startBackgroundAnimation(
                    ALPHA_ANIMATIONS_DURATION, Color.TRANSPARENT, vibrantColor
                )
                mIsTheMenuSmallVisible = true
            }
        } else {
            if (mIsTheMenuSmallVisible) {
                binding.linearlayoutMenuSmall.visibility = GONE
                binding.linearlayoutMenuSmall.startAlphaAnimation(ALPHA_ANIMATIONS_DURATION, GONE)
                binding.toolbar.startBackgroundAnimation(
                    ALPHA_ANIMATIONS_DURATION, vibrantColor, Color.TRANSPARENT
                )
                mIsTheMenuSmallVisible = false
            }
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheMenuLargeVisible) {
                this.binding.linearlayoutMenuLarge.startAlphaAnimation(
                    ALPHA_ANIMATIONS_DURATION, GONE
                )
                mIsTheMenuLargeVisible = false
            }
        } else {
            if (!mIsTheMenuLargeVisible) {
                this.binding.linearlayoutMenuLarge.startAlphaAnimation(
                    ALPHA_ANIMATIONS_DURATION, VISIBLE
                )
                mIsTheMenuLargeVisible = true
            }
        }
    }

    companion object {
        private const val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.4f
        private const val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.5f
        private const val ALPHA_ANIMATIONS_DURATION = 200L
    }
}