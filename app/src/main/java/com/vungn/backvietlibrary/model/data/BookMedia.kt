package com.vungn.backvietlibrary.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookMedia(
    val id: String,
    val bookId: String,
    val type: Int,
    val src: String,
    val size: Int,
    val title: String,
    val isMain: Boolean
) : Parcelable
