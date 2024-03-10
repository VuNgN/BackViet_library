package com.vungn.backvietlibrary.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "borrowdetail", primaryKeys = ["id"], foreignKeys = [
        ForeignKey(
            entity = BorrowEntity::class,
            parentColumns = ["id"],
            childColumns = ["borrowId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = CASCADE
        ),
    ]
)
data class BorrowDetailEntity(
    val borrowId: String,
    val bookType: Int?,
    val borrowedDate: String?,
    val returnDate: String?,
    val dueDate: String?,
    val status: Int?,
    val borrowFee: Int?,
    val depositFee: Int?,
    val latePenaltyFee: Int?,
    val bookDamagePenaltyFee: Int?,
    val bookId: String?,
    val wareHouseId: String?,
    val id: String,
    val createdDate: String?,
    val updatedDate: String?
)
