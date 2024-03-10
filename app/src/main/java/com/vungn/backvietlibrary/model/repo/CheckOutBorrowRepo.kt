package com.vungn.backvietlibrary.model.repo

import com.vungn.backvietlibrary.db.entity.BorrowEntity
import com.vungn.backvietlibrary.model.data.BorrowItem
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.service.BorrowService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import javax.inject.Inject

class CheckOutBorrowRepo @Inject constructor(
    private val borrowService: BorrowService,
) : BaseRepo<Response<BorrowItem>, BorrowEntity?>() {
    private lateinit var _borrowId: String

    override val call: Call<Response<BorrowItem>>
        get() = borrowService.checkout(_borrowId)

    suspend fun execute(
        borrowId: String, callback: Callback<Response<BorrowItem>>
    ): Flow<Response<BorrowItem>> {
        _borrowId = borrowId
        return execute(callback)
    }

    override fun getFromDatabase(): Flow<BorrowEntity?> = flow {}

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

    override suspend fun saveToDatabase(data: Response<BorrowItem>) {}
}