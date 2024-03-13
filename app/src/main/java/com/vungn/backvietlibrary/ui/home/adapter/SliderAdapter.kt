package com.vungn.backvietlibrary.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import com.vungn.backvietlibrary.databinding.ItemHomeSliderBinding

class SliderAdapter : SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {
    private var _data: List<String> = emptyList()
    var data: List<String>
        get() = _data
        set(value) {
            _data = value
            notifyDataSetChanged()
        }

    inner class SliderAdapterVH(private val binding: ItemHomeSliderBinding) :
        ViewHolder(binding.root) {
        fun setupView(item: String) {
            Glide.with(binding.root.context).load(item).fitCenter().into(binding.ivAutoImageSlider)
        }
    }

    override fun getCount(): Int = _data.size

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterVH {
        val binding =
            ItemHomeSliderBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        return SliderAdapterVH(binding)
    }

    override fun onBindViewHolder(holder: SliderAdapterVH, position: Int) {
        holder.setupView(_data[position])
    }
}