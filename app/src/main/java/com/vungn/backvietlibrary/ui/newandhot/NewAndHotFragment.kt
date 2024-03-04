package com.vungn.backvietlibrary.ui.newandhot

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.vungn.backvietlibrary.databinding.FragmentNewAndHotBinding
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.ui.activity.book.BookActivity
import com.vungn.backvietlibrary.ui.activity.main.MainActivity
import com.vungn.backvietlibrary.ui.base.FragmentBase
import com.vungn.backvietlibrary.ui.newandhot.adapter.RecycleViewAdapter
import com.vungn.backvietlibrary.ui.newandhot.contract.impl.NewAndHotViewModelImpl
import com.vungn.backvietlibrary.util.listener.OnItemClick
import kotlinx.coroutines.launch


class NewAndHotFragment : FragmentBase<FragmentNewAndHotBinding, NewAndHotViewModelImpl>() {
    private lateinit var adapter: RecycleViewAdapter
    override fun getViewBinding(): FragmentNewAndHotBinding =
        FragmentNewAndHotBinding.inflate(layoutInflater)

    override fun getViewModelClass(): Class<NewAndHotViewModelImpl> =
        NewAndHotViewModelImpl::class.java

    override fun setupListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.books.collect {
                    adapter.data = it
                    adapter.notifyItemRangeChanged(0, it.size)
                }
            }
        }
    }

    override fun setupViews() {
        (requireActivity() as MainActivity).setTopBarTitle("New & Hot")
        adapter = RecycleViewAdapter(this.requireContext())
        binding.recycleView.addItemDecoration(MaterialDividerItemDecoration(
            requireContext(), DividerItemDecoration.VERTICAL
        ).also { it.isLastItemDecorated = false })
        adapter.onItemClick = gotoBookResponse
        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private val gotoBookResponse = object : OnItemClick<BookEntity> {
        override fun onItemClick(value: BookEntity) {
            val intent = Intent(requireContext(), BookActivity::class.java)
            intent.putExtra(BookActivity.KEY_BUNDLE_BOOK, value)
            startActivity(intent)
        }
    }
}