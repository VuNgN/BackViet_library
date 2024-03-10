package com.vungn.backvietlibrary.ui.borrow.contract

import com.vungn.backvietlibrary.db.data.Cart
import com.vungn.backvietlibrary.util.enums.CallApiState
import kotlinx.coroutines.flow.StateFlow

interface BorrowViewModel {
    val carts: StateFlow<List<Cart>>
    val callApiState: StateFlow<CallApiState>
    fun getCart()
    fun deleteBorrowDetail(borrowId: String, borrowDetailId: String)
    fun checkout()
}