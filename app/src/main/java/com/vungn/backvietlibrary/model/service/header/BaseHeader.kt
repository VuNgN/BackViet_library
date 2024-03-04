package com.vungn.backvietlibrary.model.service.header

abstract class BaseHeader {
    /**
     * Convert the header to a JSON string
     */
    abstract fun toJsonString(): String
}