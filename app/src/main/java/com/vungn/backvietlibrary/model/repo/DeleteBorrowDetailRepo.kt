package com.vungn.backvietlibrary.model.repo

import com.vungn.backvietlibrary.db.dao.BorrowDetailDao
import com.vungn.backvietlibrary.db.entity.BorrowDetailEntity
import com.vungn.backvietlibrary.di.CoroutineScopeIO
import com.vungn.backvietlibrary.model.data.BorrowItem
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.service.BorrowService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject

class DeleteBorrowDetailRepo @Inject constructor(
    @CoroutineScopeIO private val coroutineScopeIO: CoroutineScope,
    private val borrowService: BorrowService,
    private val borrowDetailDao: BorrowDetailDao
) : BaseRepo<Response<BorrowItem>, List<BorrowDetailEntity>?>() {
    private lateinit var _borrowId: String
    private lateinit var _borrowDetailId: String
    override val call: Call<Response<BorrowItem>>
        get() = borrowService.deleteBorrowDetail(_borrowId, _borrowDetailId)

    suspend fun execute(
        borrowId: String, borrowDetailId: String, callback: Callback<Response<BorrowItem>>
    ): Flow<Response<BorrowItem>> {
        _borrowId = borrowId
        _borrowDetailId = borrowDetailId
        return execute(callback)
    }

    override fun getFromDatabase(): Flow<List<BorrowDetailEntity>?> = flow {}

    override fun Response<BorrowItem>.toEntity(): List<BorrowDetailEntity>? {
        val borrowItem = this.data
        val borrowDetails = borrowItem?.borrowDetails
        return borrowDetails?.map { borrowDetail ->
            BorrowDetailEntity(
                borrowId = borrowDetail.borrowId,
                bookType = borrowDetail.bookType,
                borrowedDate = borrowDetail.borrowedDate,
                returnDate = borrowDetail.returnDate,
                dueDate = borrowDetail.dueDate,
                status = borrowDetail.status,
                borrowFee = borrowDetail.borrowFee,
                depositFee = borrowDetail.depositFee,
                latePenaltyFee = borrowDetail.latePenaltyFee,
                bookDamagePenaltyFee = borrowDetail.bookDamagePenaltyFee,
                bookId = borrowDetail.bookId,
                wareHouseId = borrowDetail.wareHouseId,
                id = borrowDetail.id,
                createdDate = borrowDetail.createdDate,
                updatedDate = borrowDetail.updatedDate
            )
        }
    }

    override suspend fun saveToDatabase(data: Response<BorrowItem>) {
        coroutineScopeIO.launch(Dispatchers.IO) {
//            data.toEntity()?.let {
//                borrowDetailDao.deleteAll(it)
//            }
            _borrowDetailId.let {
                borrowDetailDao.deleteById(it)
            }
        }
    }
}