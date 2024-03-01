package com.vungn.backvietlibrary.ui.activity.main.contract

import com.vungn.backvietlibrary.model.data.CategoryData
import com.vungn.backvietlibrary.network.NetworkState
import kotlinx.coroutines.flow.StateFlow

interface MainActivityViewModel {
    val networkState: StateFlow<NetworkState>
    val avatar: StateFlow<String?>
    val categories: StateFlow<CategoryData?>
}