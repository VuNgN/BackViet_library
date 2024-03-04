package com.vungn.backvietlibrary.model.service.header

import com.google.gson.Gson

data class XQueryHeader(
    val includes: List<String>,
    val filters: List<String>,
    val sorts: List<String>,
    val page: Int,
    val pageSize: Int,
) : BaseHeader() {
    override fun toJsonString(): String {
        return Gson().toJson(this)
    }
}