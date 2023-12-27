package com.vungn.backvietlibrary.model.data

data class AuthResponse(
    val value: AuthData,
    val error: String?,
    val code: Int,
    val message: String
)