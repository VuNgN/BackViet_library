package com.vungn.backvietlibrary.ui.bookdetail.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vungn.backvietlibrary.R
import com.vungn.backvietlibrary.databinding.ItemPageViewPagerBinding
import com.vungn.backvietlibrary.util.data.Page
import com.vungn.backvietlibrary.util.gesture.ZoomInZoomOut
import com.vungn.backvietlibrary.util.listener.OnItemClick
import kotlin.math.round

class ViewPagerAdapter(private val context: Context) :
    RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    private var _data: MutableList<Page> = mutableListOf()
    private var _onItemClick: OnItemClick<Page>? = null
    private var _isZoomable: Boolean = false

    var data: MutableList<Page>
        get() = _data
        set(value) {
            _data = value.toMutableList()
        }
    var onItemClick: OnItemClick<Page>?
        get() = _onItemClick
        set(value) {
            _onItemClick = value
        }
    var isZoomable: Boolean
        get() = _isZoomable
        set(value) {
            _isZoomable = value
        }

    inner class PagerViewHolder(private val binding: ItemPageViewPagerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setupImage(bitmap: Bitmap) {
            val imageView = binding.imageView
            imageView.setImageBitmap(bitmap)
//            if (_isZoomable) {
//                imageView.setOnTouchListener(ZoomInZoomOut())
//            }
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        }

        fun setupPageInfo(index: Int) {
            val percent = round((index.toFloat() / _data.size.toFloat()) * 100).toInt()
            binding.page.text =
                context.getString(R.string.page_info, index.toString(), _data.size.toString())
            binding.percent.text = context.getString(R.string.page_percent, percent.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding =
            ItemPageViewPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(binding)
    }

    override fun getItemCount(): Int = _data.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        _data[position].apply {
            holder.setupImage(content)
            holder.setupPageInfo(page)
            holder.itemView.setOnClickListener {
                _onItemClick?.onItemClick(this)
            }
        }
    }
}