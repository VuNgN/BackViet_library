package com.vungn.backvietlibrary.ui.newandhot

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.vungn.backvietlibrary.R
import com.vungn.backvietlibrary.databinding.FragmentNewAndHotBinding
import com.vungn.backvietlibrary.model.data.Book
import com.vungn.backvietlibrary.ui.activity.auth.AuthActivity
import com.vungn.backvietlibrary.ui.activity.book.BookActivity
import com.vungn.backvietlibrary.ui.activity.search.SearchActivity
import com.vungn.backvietlibrary.ui.newandhot.adapter.RecycleViewAdapter
import com.vungn.backvietlibrary.util.books
import com.vungn.backvietlibrary.util.listener.OnItemClick


class NewAndHotFragment : Fragment() {
    private lateinit var binding: FragmentNewAndHotBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewAndHotBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUi()
        setupListener()
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

    private fun setupUi() {
        val adapter = RecycleViewAdapter(this.requireContext())
        adapter.data = books
        binding.recycleView.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        adapter.onItemClick = gotoBook
        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private val gotoBook = object : OnItemClick<Book> {
        override fun onItemClick(value: Book) {
            val intent = Intent(requireContext(), BookActivity::class.java)
            intent.putExtra(BookActivity.KEY_BUNDLE_BOOK, value)
            startActivity(intent)
        }
    }
}