package com.vungn.backvietlibrary.ui.library

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.vungn.backvietlibrary.databinding.FragmentLibraryCategoriesBinding
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.db.entity.CategoryEntity
import com.vungn.backvietlibrary.ui.activity.book.BookActivity
import com.vungn.backvietlibrary.ui.base.FragmentBase
import com.vungn.backvietlibrary.ui.library.adapter.BookCategoryAdapter
import com.vungn.backvietlibrary.ui.library.contract.impl.LibraryViewModelImpl
import com.vungn.backvietlibrary.util.GridItemDecoration
import com.vungn.backvietlibrary.util.listener.OnItemClick
import kotlinx.coroutines.launch

class LibraryCategoriesFragment :
    FragmentBase<FragmentLibraryCategoriesBinding, LibraryViewModelImpl> {
    private var category: CategoryEntity?
    private lateinit var adapter: BookCategoryAdapter

    constructor() : this(null)

    constructor(category: CategoryEntity?) {
        this.category = category
    }

    override fun getViewBinding(): FragmentLibraryCategoriesBinding =
        FragmentLibraryCategoriesBinding.inflate(layoutInflater)

    override fun getViewModelClass(): Class<LibraryViewModelImpl> = LibraryViewModelImpl::class.java

    override fun setupListener() {
        if (category == null) return
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getBooksByCategory(category!!).collect {
                    adapter.data = it
                    adapter.notifyItemRangeChanged(0, it.size)
                }
            }
        }
    }

    override fun setupViews() {
        binding.title.text = category?.name
        adapter = BookCategoryAdapter(requireActivity())
        adapter.onItemClick = gotoBookResponse
        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recycleView.addItemDecoration(GridItemDecoration(50, 2, true))
    }

    private val gotoBookResponse = object : OnItemClick<BookEntity> {
        override fun onItemClick(value: BookEntity) {
            val intent = Intent(requireContext(), BookActivity::class.java)
            intent.putExtra(BookActivity.KEY_BUNDLE_BOOK, value)
            startActivity(intent)
        }
    }
}