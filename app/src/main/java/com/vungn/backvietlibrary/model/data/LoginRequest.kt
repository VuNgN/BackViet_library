package com.vungn.backvietlibrary.model.data

data class LoginRequest(
    val password: String, val usernameOrEmail: String, val remember: Boolean
)
