package com.vungn.backvietlibrary.ui.borrow.contract.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vungn.backvietlibrary.db.data.Cart
import com.vungn.backvietlibrary.model.data.BorrowItem
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.repo.BaseRepo
import com.vungn.backvietlibrary.model.repo.CheckOutBorrowRepo
import com.vungn.backvietlibrary.model.repo.DeleteBorrowDetailRepo
import com.vungn.backvietlibrary.model.repo.GetCartRepo
import com.vungn.backvietlibrary.ui.borrow.contract.BorrowViewModel
import com.vungn.backvietlibrary.util.enums.BorrowType
import com.vungn.backvietlibrary.util.enums.CallApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BorrowViewModelImpl @Inject constructor(
    private val getCartRepo: GetCartRepo,
    private val deleteBorrowDetailRepo: DeleteBorrowDetailRepo,
    private val checkOutBorrowRepo: CheckOutBorrowRepo
) : BorrowViewModel, ViewModel() {
    private val _carts: MutableStateFlow<List<Cart>> = MutableStateFlow(emptyList())
    private val _callApiState: MutableStateFlow<CallApiState> = MutableStateFlow(CallApiState.NONE)
    override val carts: StateFlow<List<Cart>>
        get() = _carts
    override val callApiState: StateFlow<CallApiState>
        get() = _callApiState


    init {
        viewModelScope.launch(Dispatchers.IO) {
            getCartRepo.getCartToOverview().stateIn(viewModelScope).collect {
                _carts.value = it
            }
        }
    }

    override fun getCart() {
        _callApiState.value = CallApiState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            getCartRepo.execute(BorrowType.BORROW,
                object : BaseRepo.Callback<Response<BorrowItem>> {
                    override fun onSuccess(data: Response<BorrowItem>) {
                        _callApiState.value = CallApiState.SUCCESS
                    }

                    override fun onError(error: Throwable) {
                        _callApiState.value = CallApiState.ERROR
                    }

                    override fun onRelease() {}
                })
        }
    }

    override fun deleteBorrowDetail(borrowId: String, borrowDetailId: String) {
        _callApiState.value = CallApiState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            deleteBorrowDetailRepo.execute(
                borrowId,
                borrowDetailId,
                object : BaseRepo.Callback<Response<BorrowItem>> {
                    override fun onSuccess(data: Response<BorrowItem>) {
                        _callApiState.value = CallApiState.SUCCESS
                    }

                    override fun onError(error: Throwable) {
                        _callApiState.value = CallApiState.ERROR
                    }

                    override fun onRelease() {
                        getCart()
                    }
                }).launchIn(viewModelScope)
        }
    }

    override fun checkout() {
        _callApiState.value = CallApiState.LOADING
        val borrowId = _carts.value.firstOrNull()?.borrowId ?: ""
        viewModelScope.launch(Dispatchers.IO) {
            checkOutBorrowRepo.execute(borrowId, object : BaseRepo.Callback<Response<BorrowItem>> {
                override fun onSuccess(data: Response<BorrowItem>) {
                    _callApiState.value = CallApiState.SUCCESS
                }

                override fun onError(error: Throwable) {
                    _callApiState.value = CallApiState.ERROR
                }

                override fun onRelease() {
                    getCart()
                }
            }).launchIn(viewModelScope)
        }
    }
}