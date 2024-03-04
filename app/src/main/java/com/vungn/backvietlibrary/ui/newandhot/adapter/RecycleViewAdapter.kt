package com.vungn.backvietlibrary.ui.newandhot.adapter

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.vungn.backvietlibrary.databinding.ItemNewAndHotBinding
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.util.listener.OnItemClick

class RecycleViewAdapter constructor(private val context: Context) :
    RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>() {
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

        fun setupView(BookEntity: BookEntity) {
//            Glide.with(binding.root).load(BookEntity.coverImage).into(binding.bookCover)
            binding.bookName.text = BookEntity.name
            binding.bookDesc.text = BookEntity.description
        }

        fun setupListener(BookEntity: BookEntity) {
            binding.root.setOnClickListener { _onItemClick?.onItemClick(BookEntity) }
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