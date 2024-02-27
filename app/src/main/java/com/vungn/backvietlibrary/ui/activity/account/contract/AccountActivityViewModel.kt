package com.vungn.backvietlibrary.ui.activity.account.contract

import com.vungn.backvietlibrary.network.NetworkState
import kotlinx.coroutines.flow.StateFlow

interface AccountActivityViewModel {
    val networkState: StateFlow<NetworkState>
}