package com.vungn.backvietlibrary.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vungn.backvietlibrary.db.entity.BorrowDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BorrowDetailDao {
    @Query("SELECT * FROM borrowdetail")
    fun getAllBorrowDetails(): Flow<List<BorrowDetailEntity>>

    @Query("SELECT * FROM borrowdetail where borrowId = :borrowId")
    fun getBorrowDetailByBorrowId(borrowId: String): Flow<BorrowDetailEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(borrowDetails: List<BorrowDetailEntity>)

    @Query("DELETE FROM borrowdetail where id = :id")
    fun deleteById(id: String)

    @Delete
    fun delete(vararg borrowDetail: BorrowDetailEntity)

    @Delete
    fun deleteAll(borrowDetails: List<BorrowDetailEntity>)

    @Query("DELETE FROM borrowdetail")
    fun clear()
}