package com.vungn.backvietlibrary.ui.bookdetail.contract.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vungn.backvietlibrary.ui.bookdetail.contract.BookDetailViewModel
import com.vungn.backvietlibrary.util.data.Page
import com.vungn.backvietlibrary.util.state.PageUpdateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


@HiltViewModel
class BookDetailViewModelImpl @Inject constructor() : BookDetailViewModel, ViewModel() {
    private val _pages: MutableStateFlow<MutableList<Page>> = MutableStateFlow(mutableListOf())
    private val _currentPage: MutableStateFlow<Page?> = MutableStateFlow(null)
    private val _pagesCount: MutableStateFlow<Int> = MutableStateFlow(0)
    private val _updateState: MutableStateFlow<PageUpdateState> =
        MutableStateFlow(PageUpdateState.NONE)
    private lateinit var pdfRenderer: PdfRenderer
    private lateinit var parcelFileDescriptor: ParcelFileDescriptor
    override val pages: StateFlow<MutableList<Page>>
        get() = _pages
    override val currentPage: StateFlow<Page?>
        get() = _currentPage
    override val pagesCount: StateFlow<Int>
        get() = _pagesCount
    override val updateState: StateFlow<PageUpdateState>
        get() = _updateState


    override fun openRenderer(context: Context, fileName: String) {
        val file = File(context.cacheDir, fileName)
        if (!file.exists()) {
            val asset = context.assets.open(fileName)
            val output = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var size: Int = asset.read(buffer)
            while (size != -1) {
                output.write(buffer, 0, size)
                size = asset.read(buffer)
            }
            asset.close()
            output.close()
        }
        parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        pdfRenderer = PdfRenderer(parcelFileDescriptor)
        viewModelScope.launch {
            _pagesCount.emit(pdfRenderer.pageCount)
        }
    }

    override fun loadBook() {
        viewModelScope.launch {
            val pages = mutableListOf<Page>()
            _updateState.emit(PageUpdateState.UPDATING)
            for (i in 0..<pdfRenderer.pageCount) {
                val page = pdfRenderer.openPage(i)
                val bitmap: Bitmap =
                    Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page.close()
                pages.add(Page(page.index + 1, bitmap))
            }
            _pages.emit(pages)
            _currentPage.emit(_pages.value.first())
            _updateState.emit(PageUpdateState.DONE)
        }
    }

    override fun closeRenderer() {
        pdfRenderer.close()
        parcelFileDescriptor.close()
    }

    override fun updatePage(page: Page) {
        viewModelScope.launch {
            _currentPage.emit(page)
        }
    }
}