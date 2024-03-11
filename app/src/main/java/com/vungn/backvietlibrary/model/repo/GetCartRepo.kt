package com.vungn.backvietlibrary.model.repo

import com.vungn.backvietlibrary.db.dao.BorrowDao
import com.vungn.backvietlibrary.db.dao.BorrowDetailDao
import com.vungn.backvietlibrary.db.dao.CartDao
import com.vungn.backvietlibrary.db.data.Cart
import com.vungn.backvietlibrary.db.entity.BorrowDetailEntity
import com.vungn.backvietlibrary.db.entity.BorrowEntity
import com.vungn.backvietlibrary.db.entity.CartEntity
import com.vungn.backvietlibrary.di.CoroutineScopeIO
import com.vungn.backvietlibrary.model.data.BorrowDetail
import com.vungn.backvietlibrary.model.data.BorrowItem
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.service.BorrowService
import com.vungn.backvietlibrary.util.enums.BorrowType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject

class GetCartRepo @Inject constructor(
    @CoroutineScopeIO private val coroutineScopeIO: CoroutineScope,
    private val borrowService: BorrowService,
    private val cartDao: CartDao,
    private val borrowDao: BorrowDao,
    private val borrowDetailDao: BorrowDetailDao
) : BaseRepo<Response<BorrowItem>, CartEntity?>() {
    private lateinit var _borrowType: BorrowType

    override val call: Call<Response<BorrowItem>>
        get() = borrowService.getCart(_borrowType.type)

    suspend fun execute(borrowType: BorrowType, callback: Callback<Response<BorrowItem>>) {
        this._borrowType = borrowType
        execute(callback).launchIn(coroutineScopeIO)
    }

    override fun getFromDatabase(): Flow<CartEntity?> = cartDao.getCartByType(_borrowType.type)

    fun getCartToOverview(): Flow<List<Cart>> = callbackFlow {
        val job = coroutineScopeIO.launch(Dispatchers.IO) {
            cartDao.getCartWithBorrowAndBook().stateIn(coroutineScopeIO).collect {
                launch { send(it) }
            }
        }
        awaitClose { job.cancel() }
    }

    override fun Response<BorrowItem>.toEntity(): CartEntity? {
        val borrowItem = this.data
        return borrowItem?.let {
            CartEntity(
                borrowId = it.id, type = _borrowType.type
            )
        }
    }

    private fun BorrowItem.toEntity(): BorrowEntity {
        return this.let { item ->
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

    override suspend fun saveToDatabase(data: Response<BorrowItem>) {
        val borrowItem = data.data
        val borrowDetails = borrowItem?.borrowDetails
        coroutineScopeIO.launch(Dispatchers.IO) {
            borrowItem?.toEntity()?.let {
                borrowDao.insert(it)
            }
            borrowDetails?.toEntity()?.let {
                borrowDetailDao.insertAll(it)
            }
            data.toEntity()?.let {
                cartDao.clear()
                cartDao.insertCart(it)
            }
        }
    }
}