package com.vungn.backvietlibrary.model.repo

import com.vungn.backvietlibrary.di.CoroutineScopeIO
import com.vungn.backvietlibrary.model.data.PDFData
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.service.BookService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import retrofit2.Call
import javax.inject.Inject

class GetPdfRepo @Inject constructor(
    @CoroutineScopeIO private val coroutineScopeIO: CoroutineScope,
    private val bookService: BookService
) : BaseRepo<Response<PDFData>, Unit>() {
    private lateinit var _bookId: String
    private var _page: Int = 1
    private var _pageSize: Int = 1

    override val call: Call<Response<PDFData>>
        get() = bookService.getBookPdf(_bookId, _page, _pageSize)

    suspend fun getBookPdf(
        bookId: String,
        page: Int,
        pageSize: Int,
        callback: Callback<Response<PDFData>>
    ) {
        _bookId = bookId
        _page = page
        _pageSize = pageSize
        execute(callback).launchIn(coroutineScopeIO)
    }

    override fun getFromDatabase(): Flow<Unit> = flow { }

    override fun Response<PDFData>.toEntity() {}

    override suspend fun saveToDatabase(data: Response<PDFData>) {}
}