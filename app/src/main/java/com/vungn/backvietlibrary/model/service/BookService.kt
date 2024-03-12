package com.vungn.backvietlibrary.model.service

import com.vungn.backvietlibrary.model.data.BookData
import com.vungn.backvietlibrary.model.data.BookItem
import com.vungn.backvietlibrary.model.data.PDFData
import com.vungn.backvietlibrary.model.data.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface BookService {
    @GET("api/Book")
    fun getBooks(@Header("x-query") xQuery: String): Call<Response<BookData>>

    @GET("api/Book/{bookId}")
    fun getBookById(
        @Header("x-query") xQuery: String,
        @Path("bookId") id: String
    ): Call<Response<BookItem>>

    @GET("api/Book/{bookId}/pdf")
    fun getBookPdf(
        @Path("bookId") id: String,
        @Query("Page") page: Int,
        @Query("PageSize") size: Int
    ): Call<Response<PDFData>>
}