package com.vungn.backvietlibrary.ui.borrow

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.vungn.backvietlibrary.databinding.FragmentBorrowBinding
import com.vungn.backvietlibrary.db.data.Cart
import com.vungn.backvietlibrary.ui.base.FragmentBase
import com.vungn.backvietlibrary.ui.borrow.adapter.RecycleViewAdapter
import com.vungn.backvietlibrary.ui.borrow.contract.impl.BorrowViewModelImpl
import com.vungn.backvietlibrary.ui.component.CheckoutLoadingDialogFragment
import com.vungn.backvietlibrary.util.enums.CallApiState
import com.vungn.backvietlibrary.util.listener.OnItemClick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BorrowFragment : FragmentBase<FragmentBorrowBinding, BorrowViewModelImpl>() {
    private lateinit var adapter: RecycleViewAdapter
    private lateinit var checkoutDialog: CheckoutLoadingDialogFragment
    override fun getViewBinding(): FragmentBorrowBinding =
        FragmentBorrowBinding.inflate(layoutInflater)

    override fun getViewModelClass(): Class<BorrowViewModelImpl> = BorrowViewModelImpl::class.java

    override fun setupListener() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }
        binding.extendedFab.setOnClickListener {
            checkoutDialog.show(childFragmentManager, TAG)
            viewModel.checkout()
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.carts.collect {
                    launch(Dispatchers.Main) {
                        if (it.isNotEmpty() && !binding.bottomAppBar.isShown) {
                            val total = it.sumOf { cart -> cart.borrowFee ?: 0 }
                            binding.emptyView.visibility = View.GONE
                            binding.bottomAppBar.visibility = View.VISIBLE
                            binding.tvTotal.text = total.toString()
                            binding.bottomAppBar.performShow()
                        } else if (it.isEmpty()) {
                            binding.emptyView.visibility = View.VISIBLE
                            binding.bottomAppBar.performHide()
                            binding.bottomAppBar.visibility = View.GONE
                        }
                        adapter.submitList(it)
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.callApiState.collect {
                    if (checkoutDialog.isAdded) {
                        checkoutDialog.setApiState(it)
                    }
                    when (it) {
                        CallApiState.LOADING -> {
                            binding.progressBar.show()
                        }

                        CallApiState.SUCCESS -> {
                            binding.progressBar.hide()
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

    override fun setupViews() {
        setupCheckoutDialog()
        setupAdapter()
        setupRecycleView()
        setupBottomBar()
    }

    private fun setupBottomBar() {
        binding.bottomAppBar.performHide()
        binding.bottomAppBar.visibility = View.GONE
    }

    private fun setupCheckoutDialog() {
        checkoutDialog = CheckoutLoadingDialogFragment()
        checkoutDialog.isCancelable = false
    }

    private fun setupRecycleView() {
        binding.apply {
            recycleView.adapter = adapter
            recycleView.addItemDecoration(
                MaterialDividerItemDecoration(
                    requireContext(), DividerItemDecoration.VERTICAL
                ).also { it.isLastItemDecorated = false })
            recycleView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setupAdapter() {
        adapter = RecycleViewAdapter(requireContext())
        adapter.onItemClick = goToBookDetail
        adapter.onDelete = onDelete
    }

    private val goToBookDetail = object : OnItemClick<Cart> {
        override fun onItemClick(value: Cart) {
//            val intent = Intent(requireContext(), BookActivity::class.java)
//            intent.putExtra(BookActivity.KEY_BUNDLE_BOOK, value)
//            startActivity(intent)
        }
    }

    private val onDelete = object : OnItemClick<Cart> {
        override fun onItemClick(value: Cart) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc chắn muốn xóa sách `${value.bookName}` khỏi giỏ hàng?")
                .setPositiveButton("Có") { dialog, _ ->
                    viewModel.deleteBorrowDetail(value.borrowId, value.borrowDetailId)
                    dialog.dismiss()
                }
                .setNegativeButton("Không") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getCart()
    }

    companion object {
        private const val TAG = "CheckoutLoadingDialogFragment"
    }
}