package com.vungn.backvietlibrary.model.repo

import com.vungn.backvietlibrary.model.data.UserResponse
import com.vungn.backvietlibrary.model.service.UserService
import retrofit2.Call
import javax.inject.Inject

class GetUserRepo @Inject constructor(private val userService: UserService) :
    BaseRepo<UserResponse>() {
    override val call: Call<UserResponse>
        get() = userService.getUser()
}