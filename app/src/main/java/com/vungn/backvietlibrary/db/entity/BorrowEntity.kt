package com.vungn.backvietlibrary.db.entity

import androidx.room.Entity

@Entity(tableName = "borrow", primaryKeys = ["id"])
data class BorrowEntity(
    val id: String,
    val type: Int?,
    val borrowFee: Int?,
    val depositFee: Int?,
    val shipFee: Int?,
    val latePenaltyFee: Int?,
    val bookDamagePenaltyFee: Int?,
    val paymentStatus: Int?,
    val address: String?,
    val readerId: String?,
    val employeeId: String?,
    val receiveEmployeeId: String?,
    val paymentId: String?,
    val oldBorrowId: String?,
    val createDate: String?
)
