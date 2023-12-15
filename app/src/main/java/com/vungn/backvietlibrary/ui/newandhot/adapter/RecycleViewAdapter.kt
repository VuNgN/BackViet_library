package com.vungn.backvietlibrary.ui.newandhot.adapter

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vungn.backvietlibrary.databinding.ItemNewAndHotBinding
import com.vungn.backvietlibrary.model.data.Book
import com.vungn.backvietlibrary.util.listener.OnItemClick

class RecycleViewAdapter constructor(private val context: Context) :
    RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>() {
    private var _data: List<Book> = emptyList()
    private var _onItemClick: OnItemClick<Book>? = null
    var data: List<Book>
        get() = _data
        set(value) {
            _data = value
        }
    var onItemClick: OnItemClick<Book>?
        get() = _onItemClick
        set(value) {
            _onItemClick = value
        }

    inner class MyViewHolder(private val binding: ItemNewAndHotBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            val windowManager: WindowManager =
                context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val size = Point()
            windowManager.defaultDisplay.getRealSize(size)
            val width = size.x
            val targetWidth = width / 4
            val targetHeight = targetWidth / 3 * 4

            binding.bookCover.layoutParams.height = targetHeight
            binding.bookCover.layoutParams.width = targetWidth
        }

        fun setupView(book: Book) {
            Glide.with(binding.root).load(book.coverImage).into(binding.bookCover)
            binding.bookName.text = book.name
            binding.bookDesc.text = book.description
        }

        fun setupListener(book: Book) {
            binding.root.setOnClickListener { _onItemClick?.onItemClick(book) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemNewAndHotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = _data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setupView(_data[position])
        holder.setupListener(_data[position])
    }
}