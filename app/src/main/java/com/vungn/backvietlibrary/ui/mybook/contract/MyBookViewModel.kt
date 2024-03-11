package com.vungn.backvietlibrary.ui.mybook.contract

import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.util.enums.CallApiState
import kotlinx.coroutines.flow.StateFlow

interface MyBookViewModel {
    val books: StateFlow<List<BookEntity>>
    val callApiState: StateFlow<CallApiState>
    fun getBooks()
}