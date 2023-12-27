package com.vungn.backvietlibrary.ui.login.contract.impl

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vungn.backvietlibrary.model.data.AuthResponse
import com.vungn.backvietlibrary.model.repo.LoginRepo
import com.vungn.backvietlibrary.ui.login.contract.LoginViewModel
import com.vungn.backvietlibrary.util.key.PreferenceKey
import com.vungn.backvietlibrary.util.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>, private val loginRepo: LoginRepo
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
            loginRepo.login(_username.value, _password.value)
                .enqueue(object : Callback<AuthResponse> {
                    override fun onResponse(
                        call: Call<AuthResponse>, response: Response<AuthResponse>
                    ) {
                        val authResponse = response.body()
                        val accessToken = authResponse?.value?.accessToken
                        val refreshToken = authResponse?.value?.refreshToken
                        viewModelScope.launch {
                            if (response.isSuccessful) {
                                dataStore.edit { settings ->
                                    if (accessToken != null) {
                                        settings[PreferenceKey.ACCESS_TOKEN] = accessToken
                                    }
                                    if (refreshToken != null) {
                                        settings[PreferenceKey.REFRESH_TOKEN] = refreshToken
                                    }
                                }
                                _loginState.emit(LoginState.SUCCESS)
                            } else {
                                _loginState.emit(LoginState.FAIL)
                            }
                        }
                    }

                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                        Log.e(TAG, "onFailure: ", t)
                        call.cancel()
                    }
                })
        }
    }

    override fun facebookLogin() {
    }

    companion object {
        private const val TAG = "LoginViewModelImpl"
    }
}