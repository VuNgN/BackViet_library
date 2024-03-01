package com.vungn.backvietlibrary.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    val updateOnLocalDate: Long,
    @PrimaryKey
    val id: String,
    val gender: Boolean,
    val displayName: String,
    val address: String?,
    val avatar: String,
    val identityNo: String?,
    val isLock: Int
)