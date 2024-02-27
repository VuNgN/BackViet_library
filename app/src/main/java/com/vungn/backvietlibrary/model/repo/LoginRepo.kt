package com.vungn.backvietlibrary.model.repo

import com.vungn.backvietlibrary.model.data.AuthResponse
import com.vungn.backvietlibrary.model.data.DeviceInformation
import com.vungn.backvietlibrary.model.data.LoginRequest
import com.vungn.backvietlibrary.model.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import javax.inject.Inject

class LoginRepo @Inject constructor(private val userService: UserService) {
    suspend fun login(username: String, password: String): Call<AuthResponse> {
        return withContext(Dispatchers.IO) {
            userService.singIn(
                LoginRequest(
                    usernameOrEmail = username,
                    password = password,
                    remember = true,
                    deviceInfo = DeviceInformation("", "")
                )
            )
        }
    }
}