package com.vungn.backvietlibrary.ui.activity.cart.contract.impl

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.vungn.backvietlibrary.db.entity.BorrowEntity
import com.vungn.backvietlibrary.network.NetworkEvent
import com.vungn.backvietlibrary.network.NetworkState
import com.vungn.backvietlibrary.ui.activity.cart.contract.CartViewModel
import com.vungn.backvietlibrary.worker.GetAllBorrowsWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CartViewModelImpl @Inject constructor(
    networkEvent: NetworkEvent,
) : CartViewModel, ViewModel() {
    private val _networkState: StateFlow<NetworkState> = networkEvent.observableNetworkState
    private val _borrows: MutableStateFlow<BorrowEntity?> = MutableStateFlow(null)
    override val networkState: StateFlow<NetworkState>
        get() = _networkState
    override val borrows: StateFlow<BorrowEntity?>
        get() = _borrows

    override fun startWorker(context: Context) {
        val request = OneTimeWorkRequestBuilder<GetAllBorrowsWorker>()
            .build()
        WorkManager.getInstance(context).enqueue(request)
    }
}