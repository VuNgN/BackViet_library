package com.vungn.backvietlibrary.ui.addtocart.contract

import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.util.enums.CallApiState
import kotlinx.coroutines.flow.StateFlow

interface AddToCartViewModel {
    val bookEntity: StateFlow<BookEntity?>
    val borrowDate: StateFlow<Long>
    val returnDate: StateFlow<Long>
    val callApiState: StateFlow<CallApiState>

    fun setBookEntity(bookEntity: BookEntity)
    fun setBorrowDate(borrowDate: Long)
    fun setReturnDate(returnDate: Long)
    fun addToCart()
}