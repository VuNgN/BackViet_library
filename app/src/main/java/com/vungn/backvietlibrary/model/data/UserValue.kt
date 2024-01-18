package com.vungn.backvietlibrary.model.data

data class UserValue(
    val gender: Boolean,
    val displayName: String,
    val address: String,
    val birthdate: String,
    val identityNo: String,
    val phoneNumber: String,
    val devices: List<Any>,
    val avatar: String
)

