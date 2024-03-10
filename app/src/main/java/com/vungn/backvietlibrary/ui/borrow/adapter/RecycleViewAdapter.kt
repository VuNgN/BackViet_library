package com.vungn.backvietlibrary.ui.borrow.adapter

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vungn.backvietlibrary.databinding.ItemCartOverviewBinding
import com.vungn.backvietlibrary.db.data.Cart
import com.vungn.backvietlibrary.util.Common
import com.vungn.backvietlibrary.util.extension.toDate
import com.vungn.backvietlibrary.util.extension.toFormattedString
import com.vungn.backvietlibrary.util.listener.OnItemClick
import com.vungn.backvietlibrary.util.`object`.DateFormats

class RecycleViewAdapter(private val context: Context) :
    ListAdapter<Cart, RecycleViewAdapter.MyViewHolder>(CartDetailDiffCallback()) {
    private var _onItemClick: OnItemClick<Cart>? = null
    private var _onDelete: OnItemClick<Cart>? = null

    var onItemClick: OnItemClick<Cart>?
        get() = _onItemClick
        set(value) {
            _onItemClick = value
        }
    var onDelete: OnItemClick<Cart>?
        get() = _onDelete
        set(value) {
            _onDelete = value
        }

    inner class MyViewHolder(private val binding: ItemCartOverviewBinding) :
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

        fun setupView(cart: Cart) {
            Glide.with(binding.root).load(cart.coverImage).error(Common.defaultBookCover)
                .into(binding.bookCover)
            binding.bookName.text = cart.bookName
            binding.bookDesc.text = cart.description
            binding.createDate.text = cart.createDate?.toDate(DateFormats.ISO_8601)
                ?.toFormattedString(DateFormats.VIETNAMESE_DATE_TIME)
            binding.borrowDate.text = cart.borrowedDate?.toDate(DateFormats.ISO_8601)
                ?.toFormattedString(DateFormats.VIETNAMESE_DATE_TIME)
            binding.returnDate.text = cart.dueDate?.toDate(DateFormats.ISO_8601)
                ?.toFormattedString(DateFormats.VIETNAMESE_DATE_TIME)
            binding.totalPrice.text = cart.borrowFee.toString()
        }

        fun setupListener(cart: Cart) {
            binding.root.setOnClickListener { _onItemClick?.onItemClick(cart) }
            binding.deleteButton.setOnClickListener { _onDelete?.onItemClick(cart) }
        }
    }

    class CartDetailDiffCallback : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.borrowDetailId == newItem.borrowDetailId
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemCartOverviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setupView(getItem(position))
        holder.setupListener(getItem(position))
    }
}