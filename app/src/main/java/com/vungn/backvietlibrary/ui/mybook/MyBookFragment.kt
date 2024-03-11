package com.vungn.backvietlibrary.ui.mybook

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.vungn.backvietlibrary.databinding.FragmentMyBookBinding
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.ui.base.FragmentBase
import com.vungn.backvietlibrary.ui.mybook.adapter.RecycleViewAdapter
import com.vungn.backvietlibrary.ui.mybook.contract.impl.MyBookViewModelImpl
import com.vungn.backvietlibrary.util.enums.CallApiState
import com.vungn.backvietlibrary.util.listener.OnItemClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyBookFragment : FragmentBase<FragmentMyBookBinding, MyBookViewModelImpl>() {
    private lateinit var adapter: RecycleViewAdapter
    override fun getViewBinding(): FragmentMyBookBinding =
        FragmentMyBookBinding.inflate(layoutInflater)

    override fun getViewModelClass(): Class<MyBookViewModelImpl> = MyBookViewModelImpl::class.java

    override fun setupListener() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }
    }

    override fun setupViews() {
        setupAdapter()
        setupRecycleView()
        setupDataUpdate()
    }

    private fun setupDataUpdate() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.books.collect {
                    adapter.submitList(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.callApiState.collect {
                    when (it) {
                        CallApiState.LOADING -> {
                            binding.progressBar.show()
                            // Show loading
                        }

                        CallApiState.SUCCESS -> {
                            binding.progressBar.hide()
                            // Hide loading
                        }

                        CallApiState.ERROR -> {
                            binding.progressBar.hide()
                            // Hide loading
                        }

                        CallApiState.NONE -> {
                            // Do nothing
                        }
                    }
                }
            }
        }
    }

    private fun setupRecycleView() {
        binding.recycleView.apply {
            adapter = this@MyBookFragment.adapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(MaterialDividerItemDecoration(
                requireContext(), DividerItemDecoration.VERTICAL
            ).also { it.isLastItemDecorated = false })
        }
    }

    private fun setupAdapter() {
        adapter = RecycleViewAdapter(this.requireContext())
        adapter.onItemClick = onItemClick
    }

    private val onItemClick = object : OnItemClick<BookEntity> {
        override fun onItemClick(value: BookEntity) {

        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getBooks()
    }
}