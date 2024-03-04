package com.vungn.backvietlibrary.model.data

data class BookData(
    val items: List<BookItem>,
    val totalItems: Int,
    val page: Int,
    val pageSize: Int
)
