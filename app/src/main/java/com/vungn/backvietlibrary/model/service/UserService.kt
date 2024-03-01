package com.vungn.backvietlibrary.model.service

import com.vungn.backvietlibrary.model.data.AuthResponse
import com.vungn.backvietlibrary.model.data.CategoryResponse
import com.vungn.backvietlibrary.model.data.LoginRequest
import com.vungn.backvietlibrary.model.data.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    @POST("/api/Auth/login")
    fun singIn(
        @Body loginRequest: LoginRequest
    ): Call<AuthResponse>

    @GET("api/Reader")
    fun getUser(): Call<UserResponse>

    @GET("api/Category")
    fun getCategories(): Call<CategoryResponse>
}