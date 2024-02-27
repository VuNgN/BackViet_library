package com.vungn.backvietlibrary.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceInformation(
    val id: String,
    val name: String
) : Parcelable
