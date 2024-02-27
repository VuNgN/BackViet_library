package com.vungn.backvietlibrary.ui.activity.main.contract

import com.vungn.backvietlibrary.network.NetworkState
import kotlinx.coroutines.flow.StateFlow

interface MainActivityViewModel {
    val networkState: StateFlow<NetworkState>
}