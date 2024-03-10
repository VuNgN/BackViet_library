package com.vungn.backvietlibrary.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BorrowDetail(
    val borrowId: String,
    val bookType: Int? = null,
    val borrowedDate: String? = null,
    val returnDate: String? = null,
    val dueDate: String? = null,
    val status: Int? = null,
    val borrowFee: Int? = null,
    val depositFee: Int? = null,
    val latePenaltyFee: Int? = null,
    val bookDamagePenaltyFee: Int? = null,
    val bookId: String? = null,
    val wareHouseId: String? = null,
    val id: String,
    val createdDate: String? = null,
    val updatedDate: String? = null
) : Parcelable
