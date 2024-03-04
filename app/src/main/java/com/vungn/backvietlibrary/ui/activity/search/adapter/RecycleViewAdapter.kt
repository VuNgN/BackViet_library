package com.vungn.backvietlibrary.ui.activity.search.adapter

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vungn.backvietlibrary.databinding.ItemSearchBookBinding
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.util.listener.OnItemClick

class RecycleViewAdapter(private val context: Context) :
    RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>() {
    private var _data: MutableList<BookEntity> = mutableListOf()
    private var _onItemClick: OnItemClick<BookEntity>? = null
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

    inner class MyViewHolder(private val binding: ItemSearchBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setupUi(bookEntity: BookEntity) {
            val windowManager: WindowManager =
                context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val size = Point()
            windowManager.defaultDisplay.getRealSize(size)
            val width = size.x
            val targetWidth = width / 6
            val targetHeight = targetWidth / 3 * 4
            binding.title.text = bookEntity.name
            binding.author.text = bookEntity.author
            Glide.with(binding.root).load(bookEntity.coverImage)
                .apply(RequestOptions().override(targetWidth, targetHeight)).centerCrop()
                .into(binding.bookCover)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemSearchBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = _data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setupUi(_data[position])
    }
}