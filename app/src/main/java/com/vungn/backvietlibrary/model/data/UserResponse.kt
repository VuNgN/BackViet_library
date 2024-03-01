package com.vungn.backvietlibrary.model.data

data class UserResponse(
    val data: UserValue,
    val isError: Boolean,
    val message: String?
)
