package com.vungn.backvietlibrary.model.repo

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import retrofit2.Call

abstract class BaseRepo<Response> {
    abstract val call : Call<Response>
    suspend fun execute(callback: Callback<Response>): Flow<Response> = callbackFlow {
        call.enqueue(object : retrofit2.Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
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

    interface Callback<T> {
        fun onSuccess(data: T)
        fun onError(error: Throwable)
        fun onRelease()
    }
}