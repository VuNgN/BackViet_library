package com.vungn.backvietlibrary.model.repo

import com.vungn.backvietlibrary.db.dao.BorrowDao
import com.vungn.backvietlibrary.db.dao.BorrowDetailDao
import com.vungn.backvietlibrary.db.entity.BorrowDetailEntity
import com.vungn.backvietlibrary.db.entity.BorrowEntity
import com.vungn.backvietlibrary.di.CoroutineScopeIO
import com.vungn.backvietlibrary.model.data.BorrowDetail
import com.vungn.backvietlibrary.model.data.BorrowItem
import com.vungn.backvietlibrary.model.data.Request
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.service.BorrowService
import com.vungn.backvietlibrary.util.extension.toFormattedString
import com.vungn.backvietlibrary.util.`object`.DateFormats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import retrofit2.Call
import java.util.Date
import javax.inject.Inject

class AddBorrowDetailRepo @Inject constructor(
    @CoroutineScopeIO private val coroutineScopeIO: CoroutineScope,
    private val borrowService: BorrowService,
    private val getCartRepo: GetCartRepo,
    private val borrowDao: BorrowDao,
    private val borrowDetailDao: BorrowDetailDao
) : BaseRepo<Response<BorrowItem>, BorrowEntity?>() {
    private lateinit var _request: Request<BorrowItem>
    override val call: Call<Response<BorrowItem>>
        get() = borrowService.addBookToCart(_request)

    suspend fun execute(
        bookId: String, borrowDate: Long, returnDate: Long, callback: Callback<Response<BorrowItem>>
    ) {
        val borrowedDateRequest = Date(borrowDate).toFormattedString(DateFormats.ISO_8601)
        val returnDateRequest = Date(returnDate).toFormattedString(DateFormats.ISO_8601)
        getCartRepo.execute(GetCartRepo.BorrowType.BORROW, object : Callback<Response<BorrowItem>> {
            override fun onSuccess(data: Response<BorrowItem>) {
                _request = Request(
                    model = BorrowItem(
                        id = data.data?.id ?: "",
                        type = data.data?.type,
                        address = data.data?.address,
                        readerId = data.data?.readerId,
                        borrowDetails = listOf(
                            BorrowDetail(
                                bookId = bookId,
                                borrowedDate = borrowedDateRequest,
                                dueDate = returnDateRequest,
                                borrowId = data.data?.id ?: "",
                                id = "",
                                bookType = 0,
                                wareHouseId = data.data?.borrowDetails?.firstOrNull()?.wareHouseId
                            )
                        )
                    )
                )
                coroutineScopeIO.launch { execute(callback).launchIn(coroutineScopeIO) }
            }

            override fun onError(error: Throwable) {
                error.printStackTrace()
            }

            override fun onRelease() {}
        })
    }

    override fun getFromDatabase(): Flow<BorrowEntity> = flow { }

    override fun Response<BorrowItem>.toEntity(): BorrowEntity? {
        val borrowItem = this.data
        return borrowItem?.let {
            BorrowEntity(
                id = it.id,
                type = it.type,
                borrowFee = it.borrowFee,
                depositFee = it.depositFee,
                shipFee = it.shipFee,
                latePenaltyFee = it.latePenaltyFee,
                bookDamagePenaltyFee = it.bookDamagePenaltyFee,
                paymentStatus = it.paymentStatus,
                address = it.address,
                readerId = it.readerId,
                employeeId = it.employeeId,
                receiveEmployeeId = it.receiveEmployeeId,
                paymentId = it.paymentId,
                oldBorrowId = it.oldBorrowId,
                createDate = it.createdDate
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

    override suspend fun saveToDatabase(data: Response<BorrowItem>) {
        coroutineScopeIO.launch {
            data.data?.let { borrowItem ->
                val borrowDetails = borrowItem.borrowDetails
                val borrowEntity = data.toEntity()
                val borrowDetailEntities = borrowDetails?.toEntity()
                // save to database
                borrowEntity?.let {
                    borrowDao.insert(borrowEntity)
                }
                borrowDetailEntities?.let {
                    borrowDetailDao.insertAll(borrowDetailEntities)
                }
            }
        }
    }
}