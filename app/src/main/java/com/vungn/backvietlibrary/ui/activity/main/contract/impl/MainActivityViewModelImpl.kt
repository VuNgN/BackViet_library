package com.vungn.backvietlibrary.ui.activity.main.contract.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vungn.backvietlibrary.network.NetworkEvent
import com.vungn.backvietlibrary.network.NetworkState
import com.vungn.backvietlibrary.ui.activity.main.contract.MainActivityViewModel
import com.vungn.backvietlibrary.util.key.PreferenceKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModelImpl @Inject constructor(
    networkEvent: NetworkEvent,
    private val dataStore: DataStore<Preferences>
) : ViewModel(),
    MainActivityViewModel {
    private val _networkState: StateFlow<NetworkState> = networkEvent.observableNetworkState
    override val networkState: StateFlow<NetworkState>
        get() = _networkState
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
    }
}