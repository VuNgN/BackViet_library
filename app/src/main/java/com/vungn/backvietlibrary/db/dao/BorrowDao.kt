package com.vungn.backvietlibrary.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vungn.backvietlibrary.db.data.BorrowItemWithDetails
import com.vungn.backvietlibrary.db.entity.BorrowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BorrowDao {
    @Query("SELECT * FROM borrow")
    fun getAllBorrows(): Flow<List<BorrowEntity>>

    @Transaction
    @Query("SELECT * FROM borrow")
    fun getBorrowWithDetails(): Flow<List<BorrowItemWithDetails>>

    @Query("SELECT * FROM borrow where id = :id")
    fun getBorrowById(id: String): Flow<BorrowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(borrows: List<BorrowEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg borrow: BorrowEntity)

    @Query("DELETE FROM borrow")
    fun clear()
}