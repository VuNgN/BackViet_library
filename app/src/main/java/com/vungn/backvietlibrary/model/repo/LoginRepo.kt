package com.vungn.backvietlibrary.model.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.vungn.backvietlibrary.di.CoroutineScopeIO
import com.vungn.backvietlibrary.model.data.AuthResponse
import com.vungn.backvietlibrary.model.data.LoginModel
import com.vungn.backvietlibrary.model.data.Request
import com.vungn.backvietlibrary.model.service.UserService
import com.vungn.backvietlibrary.util.key.PreferenceKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject

class LoginRepo @Inject constructor(
    @CoroutineScopeIO private val coroutineScope: CoroutineScope,
    private val userService: UserService,
    private val dataStore: DataStore<Preferences>
) :
    BaseRepo<AuthResponse, Unit>() {
    private lateinit var username: String
    private lateinit var password: String
    override val call: Call<AuthResponse>
        get() = userService.singIn(
            Request(LoginModel(username, password))
        )

    suspend fun login(username: String, password: String, callback: Callback<AuthResponse>) {
        this.username = username
        this.password = password
        execute(callback).launchIn(coroutineScope)
    }

    override fun getFromDatabase(): Flow<Unit> = flow { }

    override fun AuthResponse.toEntity() = Unit

    override suspend fun saveToDatabase(data: AuthResponse) {
        val accessToken = data.data.accessToken
        val refreshToken = data.data.refreshToken
        coroutineScope.launch {
            dataStore.edit { settings ->
                settings[PreferenceKey.ACCESS_TOKEN] = accessToken
                settings[PreferenceKey.REFRESH_TOKEN] = refreshToken
            }
        }
    }
}