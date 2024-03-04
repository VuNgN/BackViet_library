package com.vungn.backvietlibrary.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Request<T : Parcelable>(
    val model: T
) : Parcelable
