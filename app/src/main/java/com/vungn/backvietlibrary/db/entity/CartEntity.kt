package com.vungn.backvietlibrary.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey val borrowId: String,
    val type: Int?,
)
