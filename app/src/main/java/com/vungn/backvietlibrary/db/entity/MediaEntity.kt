package com.vungn.backvietlibrary.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "media", foreignKeys = [ForeignKey(
        entity = BookEntity::class,
        parentColumns = ["id"],
        childColumns = ["bookId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class MediaEntity(
    @PrimaryKey val id: String,
    val bookId: String,
    val type: Int,
    val src: String,
    val size: Int,
    val title: String,
    val isMain: Boolean
)