package com.vungn.backvietlibrary.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BorrowItem(
    val id: String,
    val type: Int? = null,
    val borrowFee: Int? = null,
    val depositFee: Int? = null,
    val shipFee: Int? = null,
    val latePenaltyFee: Int? = null,
    val bookDamagePenaltyFee: Int? = null,
    val paymentStatus: Int? = null,
    val address: String? = null,
    val readerId: String? = null,
    val employeeId: String? = null,
    val receiveEmployeeId: String? = null,
    val paymentId: String? = null,
    val borrowDetails: List<BorrowDetail>? = null,
    val oldBorrowId: String? = null,
    val createdDate: String? = null
) : Parcelable
