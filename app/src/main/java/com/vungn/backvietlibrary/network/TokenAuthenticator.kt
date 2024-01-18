package com.vungn.backvietlibrary.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vungn.backvietlibrary.BuildConfig
import com.vungn.backvietlibrary.model.data.AuthResponse
import com.vungn.backvietlibrary.util.data.ApiObjectResponse
import com.vungn.backvietlibrary.util.key.PreferenceKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val networkEvent: NetworkEvent,
) : Authenticator {

    private val lock = Object()

    @ExperimentalCoroutinesApi
    override fun authenticate(route: Route?, response: Response): Request? {
        var newRequest: Request?
        synchronized(lock) {
            runBlocking {
                val refreshResult =
                    refreshToken("${BuildConfig.BASE_URL}api/Auth/refresh-token")
                newRequest = if (refreshResult) {
                    val accessToken = dataStore.data.first()[PreferenceKey.ACCESS_TOKEN]
                    response.request().newBuilder()
                        .header("Authorization", "Bearer $accessToken")
                        .build()
                } else {
                    networkEvent.publish(NetworkState.UNAUTHORIZED)
                    null
                }
            }
        }
        return newRequest
    }

    @Throws(IOException::class)
    suspend fun refreshToken(url: String): Boolean {
        val refreshUrl = URL(url)
        val urlConnection = withContext(Dispatchers.IO) {
            refreshUrl.openConnection()
        } as HttpURLConnection
        urlConnection.apply {
            doInput = true
            doOutput = true
            setRequestProperty("Accept", "application/json")
            requestMethod = "POST"
            useCaches = false
        }

        val jsonBody = JSONObject()
        val refreshToken = dataStore.data.first()[PreferenceKey.REFRESH_TOKEN] ?: ""
        jsonBody.put("refreshToken", refreshToken)

        DataOutputStream(urlConnection.outputStream).apply {
            writeBytes(jsonBody.toString())
            flush()
            close()
        }
        val responseCode = urlConnection.responseCode
        if (responseCode == 200) {
            val input = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val response = StringBuffer()
            while (true) {
                val inputLine: String =
                    withContext(Dispatchers.IO) {
                        input.readLine()
                    } ?: break
                response.append(inputLine)
            }
            withContext(Dispatchers.IO) {
                input.close()
            }
            val typeResponse =
                object : TypeToken<ApiObjectResponse<AuthResponse>>() {}.type
            val refreshTokenResult: ApiObjectResponse<AuthResponse>?
            try {
                refreshTokenResult =
                    Gson().fromJson(
                        response.toString(),
                        typeResponse
                    ) as ApiObjectResponse<AuthResponse>?
                Result.success(response)
            } catch (e: java.lang.Exception) {
                return false
            }
            if (refreshTokenResult != null) {
                with(refreshTokenResult.dataResponse) {
                    dataStore.edit { settings ->
                        settings[PreferenceKey.ACCESS_TOKEN] = this.value.accessToken
                        settings[PreferenceKey.REFRESH_TOKEN] = this.value.refreshToken
                    }
                }
            }
            return true
        } else
            return false
    }
}
