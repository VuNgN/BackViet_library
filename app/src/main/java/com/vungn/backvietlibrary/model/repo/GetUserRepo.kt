package com.vungn.backvietlibrary.model.repo

import com.vungn.backvietlibrary.model.data.UserResponse
import com.vungn.backvietlibrary.model.service.UserService
import com.vungn.backvietlibrary.util.toBearerToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import javax.inject.Inject

class GetUserRepo @Inject constructor(private val userService: UserService) {
    suspend fun getUser(token: String): Call<UserResponse> {
        val newToken = token.toBearerToken()
        return withContext(Dispatchers.IO) {
            userService.getUser(newToken)
        }
    }
}