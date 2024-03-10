package com.vungn.backvietlibrary.ui.addtocart.contract.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.model.data.BorrowItem
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.repo.AddBorrowDetailRepo
import com.vungn.backvietlibrary.model.repo.BaseRepo
import com.vungn.backvietlibrary.ui.addtocart.contract.AddToCartViewModel
import com.vungn.backvietlibrary.util.enums.CallApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddToCartViewModelImpl @Inject constructor(
    private val addBorrowDetailRepo: AddBorrowDetailRepo
) : AddToCartViewModel, ViewModel() {
    private val _bookEntity: MutableStateFlow<BookEntity?> = MutableStateFlow(null)
    private val _borrowDate: MutableStateFlow<Long> =
        MutableStateFlow(Calendar.getInstance().timeInMillis)
    private val _returnDate: MutableStateFlow<Long> =
        MutableStateFlow(Calendar.getInstance().let { calendar ->
            calendar.add(Calendar.DAY_OF_MONTH, 7)
            calendar.timeInMillis
        })
    private val _callApiState: MutableStateFlow<CallApiState> = MutableStateFlow(CallApiState.NONE)
    override val bookEntity: StateFlow<BookEntity?>
        get() = _bookEntity
    override val borrowDate: StateFlow<Long>
        get() = _borrowDate
    override val returnDate: StateFlow<Long>
        get() = _returnDate
    override val callApiState: StateFlow<CallApiState>
        get() = _callApiState

    override fun setBookEntity(bookEntity: BookEntity) {
        _bookEntity.value = bookEntity
    }

    override fun setBorrowDate(borrowDate: Long) {
        _borrowDate.value = borrowDate
    }

    override fun setReturnDate(returnDate: Long) {
        _returnDate.value = returnDate
    }

    override fun addToCart() {
        _callApiState.value = CallApiState.LOADING
        val bookId = _bookEntity.value?.id ?: return
        val borrowDate = _borrowDate.value
        val returnDate = _returnDate.value
        viewModelScope.launch(Dispatchers.IO) {
            addBorrowDetailRepo.execute(bookId,
                borrowDate,
                returnDate,
                object : BaseRepo.Callback<Response<BorrowItem>> {
                    override fun onSuccess(data: Response<BorrowItem>) {
                        _callApiState.value = CallApiState.SUCCESS
                    }

                    override fun onError(error: Throwable) {
                        _callApiState.value = CallApiState.ERROR
                    }

                    override fun onRelease() {
                        _callApiState.value = CallApiState.NONE
                    }
                })
        }
    }
}