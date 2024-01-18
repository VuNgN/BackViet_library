package com.vungn.backvietlibrary.model.data

data class UserResponse(
    val value: UserValue,
    val error: Any, // Change the type to the actual type if you know it
    val code: Int,
    val message: String
)
