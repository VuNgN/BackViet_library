package com.vungn.backvietlibrary.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.vungn.backvietlibrary.model.repo.RefreshTokenRepo
import com.vungn.backvietlibrary.util.key.PreferenceKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val refreshTokenRepo: RefreshTokenRepo
) : Authenticator {
    @ExperimentalCoroutinesApi
    override fun authenticate(route: Route?, response: Response): Request? {
        return if (response.retryCount > 4) {
            null
        } else {
            response.createSignedRequest()
        }
    }

    private val Response.retryCount: Int
        get() {
            var currentResponse = priorResponse
            var result = 0
            while (currentResponse != null) {
                result++
                currentResponse = currentResponse.priorResponse
            }
            return result
        }

    private fun Response.createSignedRequest(): Request {
        val result = refreshTokenRepo.execute()
        return if (result) {
            request.signWithToken()
        } else {
            request
        }
    }

    private fun Request.signWithToken(): Request {
        val accessToken = runBlocking {
            dataStore.data.first()[PreferenceKey.ACCESS_TOKEN]
        }
        return newBuilder()
            .removeHeader("Authorization")
            .addHeader("Authorization", "Bearer $accessToken")
            .build()
    }
}

