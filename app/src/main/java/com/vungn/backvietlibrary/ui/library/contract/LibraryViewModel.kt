package com.vungn.backvietlibrary.ui.library.contract

import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.db.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LibraryViewModel {
    val categories: StateFlow<List<CategoryEntity>>
    fun getBooksByCategory(category: CategoryEntity): Flow<List<BookEntity?>>
}