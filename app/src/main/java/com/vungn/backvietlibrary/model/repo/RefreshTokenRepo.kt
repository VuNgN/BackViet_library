package com.vungn.backvietlibrary.model.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.gson.Gson
import com.vungn.backvietlibrary.BuildConfig
import com.vungn.backvietlibrary.model.data.AuthResponse
import com.vungn.backvietlibrary.network.NetworkEvent
import com.vungn.backvietlibrary.network.NetworkState
import com.vungn.backvietlibrary.util.key.PreferenceKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class RefreshTokenRepo @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val gson: Gson,
    private val networkEvent: NetworkEvent
) {
    private val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor())
        .authenticator { _, response ->
            if (response.networkResponse?.request?.url.toString() == BuildConfig.BASE_URL + "api/Auth/refresh-token" && response.code == 401) {
                networkEvent.publish(NetworkState.UNAUTHORIZED)
            }
            null
        }.build()

    fun execute(): Boolean {
        return runBlocking {
            val refreshToken = dataStore.data.first()[PreferenceKey.REFRESH_TOKEN]
            if (refreshToken.isNullOrEmpty()) {
                networkEvent.publish(NetworkState.UNAUTHORIZED)
                return@runBlocking false
            }
            val formBody = FormBody.Builder()
                .add("refresh_token", refreshToken)
                .build()
            val request = Request.Builder().url(BuildConfig.BASE_URL + "api/Auth/refresh-token")
                .post(formBody).build()
            val call = client.newCall(request)
            val response = call.execute()
            if (response.isSuccessful) {
                val authResponse = gson.fromJson(response.body?.string(), AuthResponse::class.java)
                dataStore.edit {
                    it[PreferenceKey.ACCESS_TOKEN] = authResponse.value.accessToken
                    it[PreferenceKey.REFRESH_TOKEN] = authResponse.value.refreshToken
                }
            }
            return@runBlocking response.isSuccessful
        }
    }
}