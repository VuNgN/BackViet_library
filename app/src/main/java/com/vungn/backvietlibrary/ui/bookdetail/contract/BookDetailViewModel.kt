package com.vungn.backvietlibrary.ui.bookdetail.contract

import android.content.Context
import com.vungn.backvietlibrary.util.data.Page
import com.vungn.backvietlibrary.util.state.PageUpdateState
import kotlinx.coroutines.flow.StateFlow

interface BookDetailViewModel {
    val pages: StateFlow<MutableList<Page>>
    val currentPage: StateFlow<Page?>
    val pagesCount: StateFlow<Int>
    val updateState: StateFlow<PageUpdateState>

    fun openRenderer(context: Context, fileName: String)
    fun loadBook()
    fun closeRenderer()
    fun updatePage(page: Page)
}