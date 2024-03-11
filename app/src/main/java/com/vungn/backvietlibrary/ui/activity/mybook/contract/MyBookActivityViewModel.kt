package com.vungn.backvietlibrary.ui.activity.mybook.contract

import com.vungn.backvietlibrary.network.NetworkState
import kotlinx.coroutines.flow.StateFlow

interface MyBookActivityViewModel {
    val networkState: StateFlow<NetworkState>
}