package com.vungn.backvietlibrary.ui.login.contract.impl

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vungn.backvietlibrary.model.data.AuthResponse
import com.vungn.backvietlibrary.model.repo.BaseRepo
import com.vungn.backvietlibrary.model.repo.LoginRepo
import com.vungn.backvietlibrary.ui.login.contract.LoginViewModel
import com.vungn.backvietlibrary.util.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val loginRepo: LoginRepo,
) : LoginViewModel, ViewModel() {
    private val _username: MutableStateFlow<String> = MutableStateFlow("")
    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.NONE)
    override val username: MutableStateFlow<String>
        get() = _username
    override val password: MutableStateFlow<String>
        get() = _password
    override val loginState: StateFlow<LoginState>
        get() = _loginState

    override fun loginWithUsernameAndPassword() {
        Log.d(
            TAG,
            "loginWithUsernameAndPassword -> Username: ${_username.value}, Password: ${_password.value}"
        )
        viewModelScope.launch {
            _loginState.emit(LoginState.LOADING)
            loginRepo.login(
                _username.value,
                _password.value,
                object : BaseRepo.Callback<AuthResponse> {
                    override fun onSuccess(data: AuthResponse) {
                        _loginState.value = LoginState.SUCCESS
                    }

                    override fun onError(error: Throwable) {
                        _loginState.value = LoginState.FAIL
                    }

                    override fun onRelease() {}
                })
        }
    }

    override fun facebookLogin() {
    }

    companion object {
        private const val TAG = "LoginViewModelImpl"
    }
}