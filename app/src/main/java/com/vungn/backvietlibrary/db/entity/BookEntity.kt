package com.vungn.backvietlibrary.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book")
data class BookEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val vietnameseName: String,
    val type: Int,
    val publisherId: String,
    val authorId: String,
    val ddcCode: String,
    val warehouseCode: String,
    val reissueNo: Int,
    val publicNo: Int,
    val languages: String,
    val publishYear: Int,
    val overView: String,
    val introduction: String,
    val description: String,
    val pdfStatus: Int,
    val textStatus: Int,
    val voiceStatus: Int,
    val quantity: Int,
    val availableQuantity: Int,
    val pdfPrice: Double,
    val textPrice: Double,
    val voicePrice: Double,
    val physicalPrice: Double,
    val totalReview: Int,
    val pointReview: Double,
    val isbn10: String,
    val isbn13: String,
    val pageNumber: String,
    val viewNumber: Int,
    val borrowNumber: Int,
    val canBorrow: Boolean,
    val coverImage: String?,
    val author: String,
)