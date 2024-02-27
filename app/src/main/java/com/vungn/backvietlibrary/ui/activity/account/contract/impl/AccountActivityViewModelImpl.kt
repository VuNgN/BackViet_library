package com.vungn.backvietlibrary.ui.activity.account.contract.impl

import androidx.lifecycle.ViewModel
import com.vungn.backvietlibrary.network.NetworkEvent
import com.vungn.backvietlibrary.network.NetworkState
import com.vungn.backvietlibrary.ui.activity.account.contract.AccountActivityViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AccountActivityViewModelImpl @Inject constructor(networkEvent: NetworkEvent) : ViewModel(),
    AccountActivityViewModel {
    private val _networkState: StateFlow<NetworkState> = networkEvent.observableNetworkState
    override val networkState: StateFlow<NetworkState>
        get() = _networkState
}