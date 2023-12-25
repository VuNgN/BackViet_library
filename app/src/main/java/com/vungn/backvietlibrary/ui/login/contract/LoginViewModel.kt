package com.vungn.backvietlibrary.ui.login.contract

import kotlinx.coroutines.flow.MutableStateFlow

interface LoginViewModel {
    val username: MutableStateFlow<String>
    val password: MutableStateFlow<String>
    fun loginWithUsernameAndPassword()
    fun facebookLogin()
}