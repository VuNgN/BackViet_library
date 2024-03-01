package com.vungn.backvietlibrary.model.data

data class CategoryData(
    val items: List<CategoryItem>,
    val totalItems: Int,
    val page: Int,
    val pageSize: Int
)
