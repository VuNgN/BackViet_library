package com.vungn.backvietlibrary.util.listener

import com.vungn.backvietlibrary.model.data.Book

interface OnItemClick<T> {
    fun onItemClick(value: T)
}