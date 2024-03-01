package com.vungn.backvietlibrary.model.data

data class CategoryResponse(
    val data: CategoryData,
    val isError: Boolean,
    val message: String?
)
