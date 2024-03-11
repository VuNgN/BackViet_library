package com.vungn.backvietlibrary.model.repo

import com.vungn.backvietlibrary.db.dao.ActivatedBookDao
import com.vungn.backvietlibrary.db.dao.BookDao
import com.vungn.backvietlibrary.db.entity.ActivatedBookEntity
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.di.CoroutineScopeIO
import com.vungn.backvietlibrary.model.data.BorrowData
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.service.BorrowService
import com.vungn.backvietlibrary.util.enums.BorrowType
import com.vungn.backvietlibrary.util.extension.toDate
import com.vungn.backvietlibrary.util.`object`.DateFormats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.Call
import java.util.Calendar
import javax.inject.Inject

class GetActivatedBooksRepo @Inject constructor(
    @CoroutineScopeIO private val coroutineScopeIO: CoroutineScope,
    private val borrowService: BorrowService,
    private val activatedBookDao: ActivatedBookDao,
    private val bookDao: BookDao
) : BaseRepo<Response<BorrowData>, List<ActivatedBookEntity>?>() {
    private var _borrowType: BorrowType = BorrowType.BORROW
    override val call: Call<Response<BorrowData>>
        get() = borrowService.getExpiredBorrows(_borrowType.type)

    override suspend fun saveToDatabase(data: Response<BorrowData>) {
        coroutineScopeIO.launch(Dispatchers.IO) {
            val activatedBooks = data.toEntity()
            activatedBooks?.let { activatedBookDao.insert(it) }
        }
    }

    fun getActivatedBooks(): Flow<List<BookEntity>> = callbackFlow {
        val job = coroutineScopeIO.launch(Dispatchers.IO) {
            activatedBookDao.getAllActivatedBook().stateIn(coroutineScopeIO)
                .collect { activatedBookEntities ->
                    val ids = activatedBookEntities.map { it.bookId }
                    if (ids.isNotEmpty()) {
                        val books = bookDao.getBooksByIds(ids).stateIn(coroutineScopeIO).first()
                        launch { send(books) }
                    }
                }
        }
        awaitClose { job.cancel() }
    }

    override fun getFromDatabase(): Flow<List<ActivatedBookEntity>?> =
        activatedBookDao.getAllActivatedBook()

    override fun Response<BorrowData>.toEntity(): List<ActivatedBookEntity>? {
        val borrows = data?.items
        val borrowDetails = borrows?.flatMap { it.borrowDetails ?: emptyList() }
        val activatedBooks = borrowDetails?.map {
            ActivatedBookEntity(
                bookId = it.bookId!!,
                startDate = it.borrowedDate?.toDate(DateFormats.ISO_8601)?.time,
                endDate = it.dueDate?.toDate(DateFormats.ISO_8601)?.time
            )
        }
        return activatedBooks
    }
}