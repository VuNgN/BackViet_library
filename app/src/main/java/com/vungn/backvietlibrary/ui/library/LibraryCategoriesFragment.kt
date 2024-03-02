package com.vungn.backvietlibrary.ui.library

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.vungn.backvietlibrary.databinding.FragmentLibraryCategoriesBinding
import com.vungn.backvietlibrary.db.entity.CategoryEntity
import com.vungn.backvietlibrary.model.data.Book
import com.vungn.backvietlibrary.ui.activity.book.BookActivity
import com.vungn.backvietlibrary.ui.library.adapter.BookCategoryAdapter
import com.vungn.backvietlibrary.util.GridItemDecoration
import com.vungn.backvietlibrary.util.books
import com.vungn.backvietlibrary.util.listener.OnItemClick

class LibraryCategoriesFragment : Fragment {
    private lateinit var binding: FragmentLibraryCategoriesBinding
    private var category: CategoryEntity?

    constructor() : this(null)

    constructor(category: CategoryEntity?) {
        this.category = category
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryCategoriesBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUi()
    }

    private fun setupUi() {
        binding.title.text = category?.name
        val adapter = BookCategoryAdapter(requireContext())
        adapter.data = books
        adapter.onItemClick = gotoBook
        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recycleView.addItemDecoration(GridItemDecoration(50, 2, true))
    }

    private val gotoBook = object : OnItemClick<Book> {
        override fun onItemClick(value: Book) {
            val intent = Intent(requireContext(), BookActivity::class.java)
            intent.putExtra(BookActivity.KEY_BUNDLE_BOOK, value)
            startActivity(intent)
        }
    }
}