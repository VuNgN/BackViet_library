package com.vungn.backvietlibrary.ui.home.contract

import com.vungn.backvietlibrary.db.entity.BookEntity
import kotlinx.coroutines.flow.StateFlow

interface HomeViewModel {
    val avatar: StateFlow<String?>
    val books: StateFlow<List<BookEntity>>
}