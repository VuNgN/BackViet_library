package com.vungn.backvietlibrary.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vungn.backvietlibrary.db.data.Cart
import com.vungn.backvietlibrary.db.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart")
    fun getAllCarts(): Flow<List<CartEntity>>

    @Query("SELECT * FROM cart where borrowId = :id")
    fun getCartById(id: String): Flow<CartEntity>

    @Query("SELECT * FROM cart where type = :type")
    fun getCartByType(type: Int): Flow<CartEntity>

    @Query("SELECT borrow.id as borrowId, book.id as bookId, borrowdetail.id as borrowDetailId, borrowedDate, dueDate, createDate, name as bookName, coverImage, book.description as description, borrow.borrowFee FROM cart, borrow, borrowdetail, book Where cart.borrowId = borrow.id and borrow.id = borrowdetail.borrowId and borrowdetail.bookId = book.id")
    fun getCartWithBorrowAndBook(): Flow<List<Cart>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: CartEntity)

    @Query("DELETE FROM cart")
    suspend fun clear()
}