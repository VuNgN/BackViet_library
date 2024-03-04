package com.vungn.backvietlibrary.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookItem(
    val id: String,
    val name: String,
    val vietnameseName: String?,
    val type: Int?,
    val publisherId: String?,
    val authorId: String?,
    val ddcCode: String?,
    val warehouseCode: String?,
    val reissueNo: Int?,
    val publicNo: Int?,
    val languages: String?,
    val publishYear: Int?,
    val overView: String?,
    val introduction: String?,
    val description: String?,
    val pdfStatus: Int?,
    val textStatus: Int?,
    val voiceStatus: Int?,
    val quantity: Int?,
    val availableQuantity: Int?,
    val pdfPrice: Double?,
    val textPrice: Double?,
    val voicePrice: Double?,
    val physicalPrice: Double?,
    val totalReview: Int?,
    val pointReview: Double?,
    val isbn10: String?,
    val isbn13: String?,
    val pageNumber: String?,
    val viewNumber: Int?,
    val borrowNumber: Int?,
    val canBorrow: Boolean?,
    val medias: List<BookMedia>?
) : Parcelable
