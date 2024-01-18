package com.vungn.backvietlibrary.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.vungn.backvietlibrary.util.key.PreferenceKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class SuspendInterceptor(private val dataStore: DataStore<Preferences>) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { dataStore.data.first()[PreferenceKey.ACCESS_TOKEN] }
        if (token == null) {
            val request = chain.request().newBuilder().header("Content-Type", "application/json").build()
            return chain.proceed(request)
        }
        val request = chain.request().newBuilder().addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $token").build()
        return chain.proceed(request)
    }
}
