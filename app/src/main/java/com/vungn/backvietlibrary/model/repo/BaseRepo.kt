package com.vungn.backvietlibrary.model.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import retrofit2.Call

abstract class BaseRepo<Response, Entity> {
    /**
     * Retrofit call
     */
    abstract val call: Call<Response>

    /**
     * Execute the request and return a flow of response
     */
    suspend fun execute(callback: Callback<Response>): Flow<Response> = callbackFlow {
        call.enqueue(object : retrofit2.Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        launch(Dispatchers.IO) {
                            saveToDatabase(data)
                        }
                        callback.onSuccess(data)
                        launch { send(data) }
                    }
                } else {
                    callback.onError(Throwable(response.message()))
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                callback.onError(t)
            }
        })
        awaitClose {
            call.cancel()
        }
    }.onCompletion {
        callback.onRelease()
    }

    /**
     * Save data to SQLite database
     */
    abstract suspend fun saveToDatabase(data: Response)

    /**
     * Get data from SQLite database
     */
    abstract fun getFromDatabase(): Flow<Entity>

    /**
     * Convert response to entity
     */
    abstract fun Response.toEntity(): Entity

    /**
     * Callback for handling response
     */
    interface Callback<T> {
        /**
         * Handle success response
         */
        fun onSuccess(data: T)

        /**
         * Handle error response
         */
        fun onError(error: Throwable)

        /**
         * Handle release
         */
        fun onRelease()
    }
}