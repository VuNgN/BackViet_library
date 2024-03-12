package com.vungn.backvietlibrary.ui.bookdetail.contract.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vungn.backvietlibrary.model.data.PDFData
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.repo.BaseRepo
import com.vungn.backvietlibrary.model.repo.GetPdfRepo
import com.vungn.backvietlibrary.ui.bookdetail.contract.BookDetailViewModel
import com.vungn.backvietlibrary.util.data.Page
import com.vungn.backvietlibrary.util.enums.CallApiState
import com.vungn.backvietlibrary.util.enums.LoadFileState
import com.vungn.backvietlibrary.util.helper.FileHelper
import com.vungn.backvietlibrary.util.state.PageUpdateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookDetailViewModelImpl @Inject constructor(
    private val getPdfRepo: GetPdfRepo
) : BookDetailViewModel, ViewModel() {
    private val _pages: MutableStateFlow<MutableList<Page>> = MutableStateFlow(mutableListOf())
    private val _currentPage: MutableStateFlow<Page?> = MutableStateFlow(null)
    private val _pagesCount: MutableStateFlow<Int> = MutableStateFlow(0)
    private val _updateState: MutableStateFlow<PageUpdateState> =
        MutableStateFlow(PageUpdateState.NONE)
    private val _callApiState: MutableStateFlow<CallApiState> = MutableStateFlow(CallApiState.NONE)
    private val _loadBookState: MutableStateFlow<LoadFileState> =
        MutableStateFlow(LoadFileState.NONE)
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
    override val callApiState: StateFlow<CallApiState>
        get() = _callApiState
    override val loadBookState: StateFlow<LoadFileState>
        get() = _loadBookState


    override fun openRenderer(context: Context, bookId: String, pageCount: Int) {
        val fileName = "$bookId.pdf"
        val fileHelper = FileHelper(context, fileName)
        val file = fileHelper.getOutputPdfFile()
        if (!file.exists()) {
            _callApiState.value = CallApiState.LOADING
            viewModelScope.launch {
                loadBook(bookId, pageCount, object : BaseRepo.Callback<Response<PDFData>> {
                    override fun onSuccess(data: Response<PDFData>) {
                        _callApiState.value = CallApiState.SUCCESS
                        val item = data.data?.result
                        item?.let {
                            _loadBookState.value = LoadFileState.LOADING
                            val byte = it.fileContents
                            viewModelScope.launch(Dispatchers.IO) {
                                fileHelper.setPdfDownload(byte).stateIn(viewModelScope).first()
                                    .let { downloaded ->
                                        if (downloaded) {
                                            parcelFileDescriptor = ParcelFileDescriptor.open(
                                                file, ParcelFileDescriptor.MODE_READ_ONLY
                                            )
                                            pdfRenderer = PdfRenderer(parcelFileDescriptor)
                                            viewModelScope.launch {
                                                _pagesCount.emit(pdfRenderer.pageCount)
                                            }
                                            loadBook()
                                        }
                                    }
                            }
                        }
                    }

                    override fun onError(error: Throwable) {
                        _callApiState.value = CallApiState.ERROR
                    }

                    override fun onRelease() {}
                }).launchIn(viewModelScope)
            }
        } else {
            _loadBookState.value = LoadFileState.LOADING
            parcelFileDescriptor =
                ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            pdfRenderer = PdfRenderer(parcelFileDescriptor)
            viewModelScope.launch {
                _pagesCount.emit(pdfRenderer.pageCount)
            }
            loadBook()
        }
    }

    override fun loadBook() {
        viewModelScope.launch(Dispatchers.IO) {
            val pages = mutableListOf<Page>()
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
            _loadBookState.emit(LoadFileState.SUCCESS)
        }
    }

    private fun loadBook(
        bookId: String, pageCount: Int, callback: BaseRepo.Callback<Response<PDFData>>
    ) = callbackFlow {
        val job = viewModelScope.launch {
            getPdfRepo.getBookPdf(bookId, 1, pageCount, callback)
            launch { send(true) }
        }
        awaitClose {
            job.cancel()
        }
    }

    override fun closeRenderer() {
        try {
            pdfRenderer.close()
            parcelFileDescriptor.close()
            _pages.value.clear()
            _currentPage.value = null
            _pagesCount.value = 0
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun updatePage(page: Page) {
        viewModelScope.launch {
            _currentPage.emit(page)
        }
    }
}