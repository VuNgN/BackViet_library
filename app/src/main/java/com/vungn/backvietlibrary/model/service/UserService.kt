package com.vungn.backvietlibrary.model.service

import com.vungn.backvietlibrary.model.data.AuthResponse
import com.vungn.backvietlibrary.model.data.CategoryData
import com.vungn.backvietlibrary.model.data.LoginRequest
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.data.UserValue
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {
    @POST("/api/Auth/login")
    fun singIn(
        @Body loginRequest: LoginRequest
    ): Call<AuthResponse>

    @GET("api/Reader")
    fun getUser(): Call<Response<UserValue>>

    @GET("api/Category")
    fun getCategories(@Header("x-query") xQuery: String): Call<Response<CategoryData>>
}