package com.vungn.backvietlibrary.ui.login.contract.impl

import android.util.Log
import androidx.lifecycle.ViewModel
import com.vungn.backvietlibrary.ui.login.contract.LoginViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor() : LoginViewModel, ViewModel() {
    private val _username: MutableStateFlow<String> = MutableStateFlow("")
    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    override val username: MutableStateFlow<String>
        get() = _username
    override val password: MutableStateFlow<String>
        get() = _password

    override fun loginWithUsernameAndPassword() {
        Log.d(
            TAG,
            "loginWithUsernameAndPassword -> Username: ${_username.value}, Password: ${_password.value}"
        )
    }

    override fun facebookLogin() {
    }

    companion object {
        private const val TAG = "LoginViewModelImpl"
    }
}