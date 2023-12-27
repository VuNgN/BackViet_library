package com.vungn.backvietlibrary.ui.login.contract

import com.vungn.backvietlibrary.util.state.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface LoginViewModel {
    val username: MutableStateFlow<String>
    val password: MutableStateFlow<String>
    val loginState: StateFlow<LoginState>
    fun loginWithUsernameAndPassword()
    fun facebookLogin()
}