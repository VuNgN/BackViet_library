package com.vungn.backvietlibrary.model.data

data class AuthResponse(
    val data: AuthData,
    val isError: Boolean,
    val message: String?
)