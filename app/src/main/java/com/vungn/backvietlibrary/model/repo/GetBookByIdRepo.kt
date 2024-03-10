package com.vungn.backvietlibrary.model.repo

import com.vungn.backvietlibrary.db.dao.BookDao
import com.vungn.backvietlibrary.db.dao.MediaDao
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.db.entity.MediaEntity
import com.vungn.backvietlibrary.di.CoroutineScopeIO
import com.vungn.backvietlibrary.model.data.BookItem
import com.vungn.backvietlibrary.model.data.BookMedia
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.service.BookService
import com.vungn.backvietlibrary.model.service.header.XQueryHeader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject

class GetBookByIdRepo @Inject constructor(
    @CoroutineScopeIO private val coroutineScopeIO: CoroutineScope,
    private val bookService: BookService,
    private val bookDao: BookDao,
    private val mediaDao: MediaDao
) : BaseRepo<Response<BookItem>, BookEntity?>() {
    private lateinit var _id: String
    override val call: Call<Response<BookItem>>
        get() {
            val xQuery = XQueryHeader(
                includes = listOf("Medias"),
                filters = emptyList(),
                sorts = listOf("Id"),
                page = 1,
                pageSize = 20
            )
            return bookService.getBookById(xQuery.toJsonString(), _id)
        }

    suspend fun getBookById(id: String, callback: Callback<Response<BookItem>>) {
        _id = id
        execute(callback).launchIn(coroutineScopeIO)
    }

    fun getBookByIdFromDatabase(id: String): Flow<BookEntity> = bookDao.getBookById(id)

    override fun getFromDatabase(): Flow<BookEntity> = bookDao.getBookById(_id)

    override fun Response<BookItem>.toEntity(): BookEntity? {
        return this.data?.let { book ->
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
                coverImage = coverImage, // You need to decide how to handle this
                author = "" // You need to decide how to handle this
            )
        }
    }

    private fun List<BookMedia>.toEntity(): List<MediaEntity> {
        return this.map { media ->
            MediaEntity(
                id = media.id,
                bookId = media.bookId,
                src = media.src,
                isMain = media.isMain,
                size = media.size,
                title = media.title,
                type = media.type
            )
        }
    }

    override suspend fun saveToDatabase(data: Response<BookItem>) {
        coroutineScopeIO.launch(Dispatchers.IO) {
            val entity = data.toEntity()
            val medias = data.data?.medias?.toEntity()
            entity?.let { bookDao.insert(it) }
            medias?.let { mediaDao.insert(it) }
        }
    }
}