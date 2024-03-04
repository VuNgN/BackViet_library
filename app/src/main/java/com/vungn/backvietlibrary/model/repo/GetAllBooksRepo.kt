package com.vungn.backvietlibrary.model.repo

import com.vungn.backvietlibrary.db.dao.BookDao
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.di.CoroutineScopeIO
import com.vungn.backvietlibrary.model.data.BookData
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.service.BookService
import com.vungn.backvietlibrary.model.service.header.XQueryHeader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject

class GetAllBooksRepo @Inject constructor(
    @CoroutineScopeIO private val coroutineScopeIO: CoroutineScope,
    private val service: BookService,
    private val bookDao: BookDao
) : BaseRepo<Response<BookData>, List<BookEntity>>() {
    override val call: Call<Response<BookData>>
        get() {
            val xQuery = XQueryHeader(
                includes = listOf("Medias"),
                filters = emptyList(),
                sorts = listOf("Id"),
                page = 1,
                pageSize = Int.MAX_VALUE
            )
            return service.getBooks(xQuery = xQuery.toJsonString())
        }

    override fun getFromDatabase(): Flow<List<BookEntity>> = bookDao.getAllBooks()

    override fun Response<BookData>.toEntity(): List<BookEntity> {
        val books = this.data.items
        return books.map { book ->
            val coverImage = book.medias?.find { it.isMain }?.src
            BookEntity(
                id = book.id,
                name = book.name,
                vietnameseName = book.vietnameseName,
                type = book.type,
                publisherId = book.publisherId,
                authorId = book.authorId,
                ddcCode = book.ddcCode,
                warehouseCode = book.warehouseCode,
                reissueNo = book.reissueNo,
                publicNo = book.publicNo,
                languages = book.languages,
                publishYear = book.publishYear,
                overView = book.overView,
                introduction = book.introduction,
                description = book.description,
                pdfStatus = book.pdfStatus,
                textStatus = book.textStatus,
                voiceStatus = book.voiceStatus,
                quantity = book.quantity,
                availableQuantity = book.availableQuantity,
                pdfPrice = book.pdfPrice,
                textPrice = book.textPrice,
                voicePrice = book.voicePrice,
                physicalPrice = book.physicalPrice,
                totalReview = book.totalReview,
                pointReview = book.pointReview,
                isbn10 = book.isbn10,
                isbn13 = book.isbn13,
                pageNumber = book.pageNumber,
                viewNumber = book.viewNumber,
                borrowNumber = book.borrowNumber,
                canBorrow = book.canBorrow,
                coverImage = coverImage,
                author = "" // You need to decide how to handle this
            )
        }
    }

    override suspend fun saveToDatabase(data: Response<BookData>) {
        coroutineScopeIO.launch(Dispatchers.IO) {
            bookDao.insert(data.toEntity())
        }
    }
}