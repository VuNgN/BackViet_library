package com.vungn.backvietlibrary.model.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginRequest(
    val password: String,
    val usernameOrEmail: String,
    val remember: Boolean,
    @SerializedName("deviceInfor")
    val deviceInfo: DeviceInformation
) : Parcelable
