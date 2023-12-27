package com.vungn.backvietlibrary.model.service

import com.vungn.backvietlibrary.model.data.AuthResponse
import com.vungn.backvietlibrary.model.data.LoginRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

public interface UserService {
    @POST("/api/Auth/login")
    fun singIn(
        @Body loginRequest: LoginRequest
    ): Call<AuthResponse>
}