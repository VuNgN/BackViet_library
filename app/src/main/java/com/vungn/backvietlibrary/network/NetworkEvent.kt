package com.vungn.backvietlibrary.network

import com.google.android.gms.common.api.ApiException
import com.vungn.backvietlibrary.di.CoroutineScopeMain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

sealed class NetworkState {
    object CONNECTED : NetworkState()
    object NO_INTERNET : NetworkState()
    object CONNECTION_LOST : NetworkState()
    object FORBIDDEN : NetworkState()
    object SERVER_NOT_AVAILABLE : NetworkState()
    object UNAUTHORIZED : NetworkState()
    object INITIALIZE : NetworkState()
    object ERROR : NetworkState()
    object NOT_FOUND : NetworkState()
    object BAD_REQUEST : NetworkState()
    data class GENERIC(val exception: ApiException) : NetworkState()
}

@Singleton
class NetworkEvent @Inject constructor(
    @CoroutineScopeMain private val coroutineScopeMain: CoroutineScope,
) {
    private val _observableNetworkState: MutableStateFlow<NetworkState> =
        MutableStateFlow(NetworkState.INITIALIZE)
    val observableNetworkState: StateFlow<NetworkState> get() = _observableNetworkState

    fun publish(networkState: NetworkState) {
        coroutineScopeMain.launch {
            _observableNetworkState.emit(networkState)
        }
    }
}
