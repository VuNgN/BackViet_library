package com.vungn.backvietlibrary.ui.activity.cart.contract

import android.content.Context
import com.vungn.backvietlibrary.db.entity.BorrowEntity
import com.vungn.backvietlibrary.network.NetworkState
import kotlinx.coroutines.flow.StateFlow

interface CartViewModel {
    val networkState: StateFlow<NetworkState>
    val borrows: StateFlow<BorrowEntity?>
    fun startWorker(context: Context)
}