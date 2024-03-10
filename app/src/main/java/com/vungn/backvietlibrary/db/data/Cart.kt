package com.vungn.backvietlibrary.db.data

data class Cart(
    val borrowId: String,
    val bookId: String,
    val borrowDetailId: String,
    val borrowedDate: String?,
    val dueDate: String?,
    val createDate: String?,
    val bookName: String?,
    val coverImage: String?,
    val description: String?,
    val borrowFee: Int?,
)