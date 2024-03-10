package com.vungn.backvietlibrary.model.service

import com.vungn.backvietlibrary.model.data.BorrowData
import com.vungn.backvietlibrary.model.data.BorrowItem
import com.vungn.backvietlibrary.model.data.Request
import com.vungn.backvietlibrary.model.data.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BorrowService {
    @GET("api/Borrow")
    fun getBorrows(@Header("x-query") xQuery: String): Call<Response<BorrowData>>

    @GET("/api/Borrow/cart/{borrowType}")
    fun getCart(@Path("borrowType") borrowType: Int): Call<Response<BorrowItem>>

    @PUT("/api/Borrow/{id}/checkout")
    fun checkout(@Path("id") borrowId: String): Call<Response<BorrowItem>>

    @POST("/api/Borrow/add-book-to-cart")
    fun addBookToCart(@Body request: Request<BorrowItem>): Call<Response<BorrowItem>>

    @DELETE("/api/Borrow/{borrowId}/borrow-detail/{borrowDetailId}")
    fun deleteBorrowDetail(
        @Path("borrowId") borrowId: String,
        @Path("borrowDetailId") borrowDetailId: String
    ): Call<Response<BorrowItem>>
}