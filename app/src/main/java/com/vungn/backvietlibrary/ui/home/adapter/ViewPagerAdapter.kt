package com.vungn.backvietlibrary.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.vungn.backvietlibrary.databinding.ItemViewPagerBinding
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.util.Common
import com.vungn.backvietlibrary.util.listener.OnItemClick

class ViewPagerAdapter(private val context: Context, viewPager2: ViewPager2) :
    RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    private var _data: MutableList<BookEntity> = mutableListOf()
    private var _onItemClick: OnItemClick<BookEntity>? = null
    private val doubleData: Runnable = Runnable {
        _data.addAll(_data)
        notifyItemRangeChanged(_data.size - 1, _data.size)
    }
    private val _viewPager2: ViewPager2

    var data: List<BookEntity>
        get() = _data
        set(value) {
            _data = value.toMutableList()
        }
    var onItemClick: OnItemClick<BookEntity>?
        get() = _onItemClick
        set(value) {
            _onItemClick = value
        }

    init {
        _viewPager2 = viewPager2
    }

    inner class PagerViewHolder(private val binding: ItemViewPagerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setupImage(imageUri: String) {
            val imageView = binding.imageView
            Glide.with(context).load(imageUri).error(Common.defaultBookCover).centerCrop()
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding =
            ItemViewPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(binding)
    }

    override fun getItemCount(): Int = _data.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.setupImage(data[position].coverImage ?: Common.defaultBookCover)
        holder.itemView.setOnClickListener {
            _onItemClick?.onItemClick(_data[position])
        }
        if (position == _data.size - 2) {
            _viewPager2.post(doubleData)
        }
    }
}