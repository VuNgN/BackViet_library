package com.vungn.backvietlibrary.model.data

data class BorrowData(
    val items: List<BorrowItem>,
    val totalItems: Int,
    val page: Int,
    val pageSize: Int
)
