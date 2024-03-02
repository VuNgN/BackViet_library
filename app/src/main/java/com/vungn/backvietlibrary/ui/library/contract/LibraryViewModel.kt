package com.vungn.backvietlibrary.ui.library.contract

import com.vungn.backvietlibrary.db.entity.CategoryEntity
import kotlinx.coroutines.flow.StateFlow

interface LibraryViewModel {
    val categories: StateFlow<List<CategoryEntity>>
}