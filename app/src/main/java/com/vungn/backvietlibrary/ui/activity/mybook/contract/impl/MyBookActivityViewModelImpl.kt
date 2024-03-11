package com.vungn.backvietlibrary.ui.activity.mybook.contract.impl

import androidx.lifecycle.ViewModel
import com.vungn.backvietlibrary.network.NetworkEvent
import com.vungn.backvietlibrary.network.NetworkState
import com.vungn.backvietlibrary.ui.activity.mybook.contract.MyBookActivityViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MyBookActivityViewModelImpl @Inject constructor(networkEvent: NetworkEvent) :
    MyBookActivityViewModel, ViewModel() {
    private val _networkState: StateFlow<NetworkState> = networkEvent.observableNetworkState
    override val networkState: StateFlow<NetworkState>
        get() = _networkState
}