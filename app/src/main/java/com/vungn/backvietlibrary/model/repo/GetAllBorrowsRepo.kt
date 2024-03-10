package com.vungn.backvietlibrary.model.repo

import com.vungn.backvietlibrary.db.dao.BorrowDao
import com.vungn.backvietlibrary.db.dao.BorrowDetailDao
import com.vungn.backvietlibrary.db.entity.BorrowDetailEntity
import com.vungn.backvietlibrary.db.entity.BorrowEntity
import com.vungn.backvietlibrary.di.CoroutineScopeIO
import com.vungn.backvietlibrary.model.data.BorrowData
import com.vungn.backvietlibrary.model.data.BorrowDetail
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.service.BorrowService
import com.vungn.backvietlibrary.model.service.header.XQueryHeader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject

class GetAllBorrowsRepo @Inject constructor(
    @CoroutineScopeIO private val coroutineScopeIO: CoroutineScope,
    private val borrowService: BorrowService,
    private val borrowDao: BorrowDao,
    private val borrowDetailDao: BorrowDetailDao
) : BaseRepo<Response<BorrowData>, List<BorrowEntity>?>() {
    private var _page = 1
    private var _pageSize = 10
    override val call: Call<Response<BorrowData>>
        get() {
            val header = XQueryHeader(
                includes = listOf("BorrowDetails.Book"),
                filters = emptyList(),
                sorts = emptyList(),
                page = _page,
                pageSize = _pageSize
            )
            return borrowService.getBorrows(header.toJsonString())
        }

    suspend fun execute(page: Int, pageSite: Int, callback: Callback<Response<BorrowData>>) {
        _page = page
        _pageSize = pageSite
        execute(callback).launchIn(coroutineScopeIO)
    }

    override fun getFromDatabase(): Flow<List<BorrowEntity>?> = borrowDao.getAllBorrows()

    override fun Response<BorrowData>.toEntity(): List<BorrowEntity>? {
        val borrowData = this.data
        return borrowData?.items?.map { item ->
            BorrowEntity(
                id = item.id,
                type = item.type,
                borrowFee = item.borrowFee,
                depositFee = item.depositFee,
                shipFee = item.shipFee,
                latePenaltyFee = item.latePenaltyFee,
                bookDamagePenaltyFee = item.bookDamagePenaltyFee,
                paymentStatus = item.paymentStatus,
                address = item.address,
                readerId = item.readerId,
                employeeId = item.employeeId,
                receiveEmployeeId = item.receiveEmployeeId,
                paymentId = item.paymentId,
                oldBorrowId = item.oldBorrowId,
                createDate = item.createdDate
            )
        }
    }

    private fun List<BorrowDetail>.toEntity(): List<BorrowDetailEntity> {
        return this.map { detail ->
            BorrowDetailEntity(
                borrowId = detail.borrowId,
                bookType = detail.bookType,
                borrowedDate = detail.borrowedDate,
                returnDate = detail.returnDate,
                dueDate = detail.dueDate,
                status = detail.status,
                borrowFee = detail.borrowFee,
                depositFee = detail.depositFee,
                latePenaltyFee = detail.latePenaltyFee,
                bookDamagePenaltyFee = detail.bookDamagePenaltyFee,
                bookId = detail.bookId,
                wareHouseId = detail.wareHouseId,
                id = detail.id,
                createdDate = detail.createdDate,
                updatedDate = detail.updatedDate
            )
        }
    }

    override suspend fun saveToDatabase(data: Response<BorrowData>) {
        val borrowData = data.data
        val borrowItems = borrowData?.items
        val borrowDetails = borrowItems?.flatMap { it.borrowDetails ?: emptyList() }
        coroutineScopeIO.launch(Dispatchers.IO) {
            data.toEntity()?.let { borrowDao.insertAll(it) }
            borrowDetails?.toEntity()?.let { borrowDetailDao.insertAll(it) }
        }
    }
}