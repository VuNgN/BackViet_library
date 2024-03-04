package com.vungn.backvietlibrary.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.vungn.backvietlibrary.databinding.ItemCarouselBookCategoryBinding
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.util.Common
import com.vungn.backvietlibrary.util.listener.OnItemClick

class CarouselBookCategoryAdapter(private val context: Context) :
    Adapter<CarouselBookCategoryAdapter.CarouselViewHolder>() {
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

    inner class CarouselViewHolder(private val binding: ItemCarouselBookCategoryBinding) :
        ViewHolder(binding.root) {
        fun loadImage(url: String) {
            Glide.with(context).load(url).error(Common.defaultBookCover)
                .into(binding.carouselImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = ItemCarouselBookCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CarouselViewHolder(binding)
    }

    override fun getItemCount(): Int = _data.size

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.loadImage(_data[position].coverImage ?: Common.defaultBookCover)
        holder.itemView.setOnClickListener {
            _onItemClick?.onItemClick(_data[position])
        }
    }
}