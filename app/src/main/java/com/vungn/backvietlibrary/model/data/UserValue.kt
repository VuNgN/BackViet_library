package com.vungn.backvietlibrary.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserValue(
    val id: String,
    val gender: Boolean?,
    val displayName: String?,
    val address: String?,
    val avatar: String?,
    val identityNo: String?,
    val isLock: Int?
) : Parcelable
