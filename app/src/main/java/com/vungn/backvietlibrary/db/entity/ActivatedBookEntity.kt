package com.vungn.backvietlibrary.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activated_book")
data class ActivatedBookEntity(
    @PrimaryKey
    val bookId: String,
    val startDate: Long?,
    val endDate: Long?
)