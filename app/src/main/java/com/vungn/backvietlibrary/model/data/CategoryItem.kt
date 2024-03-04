package com.vungn.backvietlibrary.model.data

data class CategoryItem(
    val id: String,
    val name: String,
    val books: List<BookItem>?
)
