package com.vungn.backvietlibrary.ui.newandhot.contract

import com.vungn.backvietlibrary.db.entity.BookEntity
import kotlinx.coroutines.flow.StateFlow

interface NewAndHotViewModel {
    val books: StateFlow<List<BookEntity>>
}