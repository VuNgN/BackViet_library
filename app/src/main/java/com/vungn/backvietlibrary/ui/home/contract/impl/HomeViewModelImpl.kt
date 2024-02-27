package com.vungn.backvietlibrary.ui.home.contract.impl

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vungn.backvietlibrary.network.NetworkEvent
import com.vungn.backvietlibrary.network.NetworkState
import com.vungn.backvietlibrary.ui.home.contract.HomeViewModel
import com.vungn.backvietlibrary.util.key.PreferenceKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    dataStore: DataStore<Preferences>,
    networkEvent: NetworkEvent,
) : HomeViewModel, ViewModel() {
    private val _avatar: MutableStateFlow<String?> = MutableStateFlow(null)
    override val avatar: StateFlow<String?>
        get() = _avatar

    init {
        viewModelScope.launch {
            dataStore.data.collect { preferences ->
                val avatar = preferences[PreferenceKey.AVATAR_URL]
                _avatar.emit(avatar)
            }
        }
        viewModelScope.launch {
            networkEvent.observableNetworkState.collect {
                when (it) {
                    is NetworkState.CONNECTED -> {
                        Log.d(TAG, "network state: CONNECTED")
                    }

                    is NetworkState.UNAUTHORIZED -> {
                        Log.d(TAG, "network state: UNAUTHORIZED")
                    }

                    is NetworkState.NO_INTERNET -> {
                        Log.d(TAG, "network state: NO_INTERNET")
                    }

                    is NetworkState.CONNECTION_LOST -> {
                        Log.d(TAG, "network state: CONNECTION_LOST")
                    }

                    is NetworkState.FORBIDDEN -> {
                        Log.d(TAG, "network state: FORBIDDEN")
                    }

                    is NetworkState.SERVER_NOT_AVAILABLE -> {
                        Log.d(TAG, "network state: SERVER_NOT_AVAILABLE")
                    }

                    is NetworkState.INITIALIZE -> {
                        Log.d(TAG, "network state: INITIALIZE")
                    }

                    is NetworkState.ERROR -> {
                        Log.d(TAG, "network state: ERROR")
                    }

                    is NetworkState.NOT_FOUND -> {
                        Log.d(TAG, "network state: NOT_FOUND")
                    }

                    is NetworkState.BAD_REQUEST -> {
                        Log.d(TAG, "network state: BAD_REQUEST")
                    }

                    is NetworkState.GENERIC -> {
                        Log.d(TAG, "network state: GENERIC")
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModelImpl"
    }
}