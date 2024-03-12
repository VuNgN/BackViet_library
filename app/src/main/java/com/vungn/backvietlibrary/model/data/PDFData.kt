package com.vungn.backvietlibrary.model.data

data class PDFData(
    val result: PDFItem,
    val totalPage: Int,
    val page: Int,
    val pageSize: Int
)