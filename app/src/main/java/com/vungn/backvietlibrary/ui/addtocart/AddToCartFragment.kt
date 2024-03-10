package com.vungn.backvietlibrary.ui.addtocart

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import androidx.core.util.Pair
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.vungn.backvietlibrary.databinding.FragmentAddToCartBinding
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.ui.addtocart.contract.impl.AddToCartViewModelImpl
import com.vungn.backvietlibrary.ui.base.FragmentBase
import com.vungn.backvietlibrary.util.Common
import com.vungn.backvietlibrary.util.enums.CallApiState
import com.vungn.backvietlibrary.util.extension.toDateString
import com.vungn.backvietlibrary.util.`object`.DateFormats
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddToCartFragment : FragmentBase<FragmentAddToCartBinding, AddToCartViewModelImpl>() {
    private var bookEntity: BookEntity? = null
    private lateinit var picker: MaterialDatePicker<Pair<Long, Long>>
    private val args: AddToCartFragmentArgs by navArgs()
    override fun getViewBinding(): FragmentAddToCartBinding =
        FragmentAddToCartBinding.inflate(layoutInflater)

    override fun getViewModelClass(): Class<AddToCartViewModelImpl> =
        AddToCartViewModelImpl::class.java

    override fun setupListener() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }
        binding.pickDateButton.setOnClickListener {
            picker.show(childFragmentManager, TAG)
        }
        picker.addOnPositiveButtonClickListener {
            val borrowDate = it.first
            val returnDate = it.second
            viewModel.setBorrowDate(borrowDate)
            viewModel.setReturnDate(returnDate)
        }
        binding.extendedFab.setOnClickListener {
            viewModel.addToCart()
        }
    }

    override fun setupViews() {
        picker = MaterialDatePicker.Builder.dateRangePicker().build()
        bookEntity = args.book
        viewModel.setBookEntity(bookEntity!!)
        setupImageView()
        binding.apply {
            Glide.with(binding.root).load(bookEntity?.coverImage).error(Common.defaultBookCover)
                .into(binding.bookCover)
            bookName.text = bookEntity?.name
            bookDesc.text = bookEntity?.description
        }
        viewModel.apply {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    borrowDate.collect {
                        binding.borrowDate.text = it.toDateString(DateFormats.VIETNAMESE_DATE_TIME)
                    }
                }
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    returnDate.collect {
                        binding.returnDate.text = it.toDateString(DateFormats.VIETNAMESE_DATE_TIME)
                    }
                }
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    callApiState.collect {
                        when (it) {
                            CallApiState.LOADING -> {
                                binding.progressBar.show()
                            }

                            CallApiState.SUCCESS -> {
                                binding.progressBar.hide()
                                navController.popBackStack()
                            }

                            CallApiState.ERROR -> {
                                binding.progressBar.hide()
                            }

                            CallApiState.NONE -> {
                                binding.progressBar.hide()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupImageView() {
        val windowManager: WindowManager =
            requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        windowManager.defaultDisplay.getRealSize(size)
        val width = size.x
        val targetWidth = width / 4
        val targetHeight = targetWidth / 3 * 4

        binding.bookCover.layoutParams.height = targetHeight
        binding.bookCover.layoutParams.width = targetWidth
    }

    companion object {
        const val TAG = "AddToCartFragment"
    }
}