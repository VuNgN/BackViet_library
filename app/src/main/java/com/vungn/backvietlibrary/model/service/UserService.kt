package com.vungn.backvietlibrary.model.service

import com.vungn.backvietlibrary.model.data.AuthResponse
import com.vungn.backvietlibrary.model.data.CategoryData
import com.vungn.backvietlibrary.model.data.LoginModel
import com.vungn.backvietlibrary.model.data.Request
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.data.UserValue
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserService {
    @POST("/api/Auth/login")
    fun singIn(
        @Body request: Request<LoginModel>
    ): Call<AuthResponse>

    @GET("api/Reader")
    fun getUser(): Call<Response<UserValue>>

    @PUT("api/Reader")
    fun updateUser(@Body request: Request<UserValue>): Call<Response<UserValue>>

    @GET("api/Category")
    fun getCategories(@Header("x-query") xQuery: String): Call<Response<CategoryData>>
}