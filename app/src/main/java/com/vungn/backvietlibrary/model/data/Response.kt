package com.vungn.backvietlibrary.model.data

data class Response<T>(
    val data: T,
    val isError: Boolean,
    val message: String?
)
