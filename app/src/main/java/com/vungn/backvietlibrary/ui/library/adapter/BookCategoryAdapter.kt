package com.vungn.backvietlibrary.ui.library.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vungn.backvietlibrary.databinding.ItemBooksOfLibraryCategoryBinding
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.util.Common
import com.vungn.backvietlibrary.util.listener.OnItemClick


class BookCategoryAdapter(private val activity: Activity) :
    RecyclerView.Adapter<BookCategoryAdapter.BookCategoryViewHolder>() {
    private var _data: List<BookEntity> = emptyList()
    private var _onItemClick: OnItemClick<BookEntity>? = null
    var data: List<BookEntity>
        get() = _data
        set(value) {
            _data = value
        }
    var onItemClick: OnItemClick<BookEntity>?
        get() = _onItemClick
        set(value) {
            _onItemClick = value
        }

    inner class BookCategoryViewHolder(private val binding: ItemBooksOfLibraryCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var targetWidth: Int = 0
        private var targetHeight: Int = 0

        init {
            val windowManager: WindowManager =
                activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val size = Point()
            windowManager.defaultDisplay.getRealSize(size)
            val width = size.x
            targetWidth = width / 2
            targetHeight = targetWidth / 3 * 4

            binding.cardView.layoutParams.width = targetWidth
            binding.cardView.layoutParams.height = targetHeight
        }

        fun setupUi(bookEntity: BookEntity) {
            binding.apply {
                title.text = bookEntity.name
                sub.text = bookEntity.description
            }
        }

        fun setupImage(src: String) {
            val imageView = binding.imageView
            Glide.with(activity.applicationContext).load(src)
                .apply(RequestOptions().override(targetWidth, targetHeight)).centerCrop()
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookCategoryViewHolder {
        val binding =
            ItemBooksOfLibraryCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return BookCategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = _data.size

    override fun onBindViewHolder(holder: BookCategoryViewHolder, position: Int) {
        holder.setupImage(_data[position].coverImage ?: Common.defaultBookCover)
        holder.setupUi(_data[position])
        holder.itemView.setOnClickListener {
            _onItemClick?.onItemClick(_data[position])
        }
    }
}