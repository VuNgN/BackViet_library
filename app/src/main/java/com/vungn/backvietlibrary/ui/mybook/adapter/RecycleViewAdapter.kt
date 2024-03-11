package com.vungn.backvietlibrary.ui.mybook.adapter

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vungn.backvietlibrary.databinding.ItemVerticalBookListBinding
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.util.Common
import com.vungn.backvietlibrary.util.listener.OnItemClick

class RecycleViewAdapter(private val context: Context) :
    ListAdapter<BookEntity, RecycleViewAdapter.MyViewHolder>(DiffCallback()) {
    private var _onItemClick: OnItemClick<BookEntity>? = null
    var onItemClick: OnItemClick<BookEntity>?
        get() = _onItemClick
        set(value) {
            _onItemClick = value
        }

    inner class MyViewHolder(private val binding: ItemVerticalBookListBinding) :
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

        fun setupView(bookEntity: BookEntity) {
            Glide.with(binding.root).load(bookEntity.coverImage).error(Common.defaultBookCover)
                .into(binding.bookCover)
            binding.bookName.text = bookEntity.name
            binding.bookDesc.text = bookEntity.description
            binding.dateTime.text = "Xuất bản năm: ${bookEntity.publishYear}"
        }

        fun setupListener(bookEntity: BookEntity) {
            binding.root.setOnClickListener { _onItemClick?.onItemClick(bookEntity) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<BookEntity>() {
        override fun areItemsTheSame(oldItem: BookEntity, newItem: BookEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BookEntity, newItem: BookEntity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemVerticalBookListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setupView(getItem(position))
        holder.setupListener(getItem(position))
    }
}