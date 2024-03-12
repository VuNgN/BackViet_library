package com.vungn.backvietlibrary.model.data

data class PDFItem (
    val fileContents: String,
    val contentType: String,
    val fileDownloadName: String,
    val lastModified: Any?,
    val entityTag: Any?,
    val enableRangeProcessing: Boolean
)