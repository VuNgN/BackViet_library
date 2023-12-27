package com.vungn.backvietlibrary.model.data

import com.google.gson.annotations.SerializedName

data class AuthData(
    val accessToken: String,
    val refreshToken: String,
    val expiresRefreshToken: String,
    @SerializedName("expiress")
    val expired: String
)