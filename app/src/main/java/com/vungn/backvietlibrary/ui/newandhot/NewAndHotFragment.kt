package com.vungn.backvietlibrary.ui.newandhot

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.vungn.backvietlibrary.databinding.FragmentNewAndHotBinding
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.ui.activity.book.BookActivity
import com.vungn.backvietlibrary.ui.activity.main.MainActivity
import com.vungn.backvietlibrary.ui.newandhot.adapter.RecycleViewAdapter
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
    }

    private fun setupUi() {
        (requireActivity() as MainActivity).setTopBarTitle("New & Hot")
        val adapter = RecycleViewAdapter(this.requireContext())
//        adapter.data = bookResponses
        binding.recycleView.addItemDecoration(
            MaterialDividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            ).also { it.isLastItemDecorated = false }
        )
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