package com.vungn.backvietlibrary.model.data

data class UserValue(
    val createdDate: String,
    val updatedDate: String,
    val gender: Boolean,
    val displayName: String,
    val address: String?,
    val birthdate: String?,
    val identityNo: String?,
    val wallet: Int,
    val avatar: String?,
    val isLock: Int,
    val paymentMethod: Any?,
    val locationId: Any?,
    val isLinkAccount: Boolean,
    val email: String,
    val userName: String,
    val phoneNumber: String?,
    val currentCardId: Any?
)

