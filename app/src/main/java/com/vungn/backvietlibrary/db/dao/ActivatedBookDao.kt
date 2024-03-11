package com.vungn.backvietlibrary.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vungn.backvietlibrary.db.entity.ActivatedBookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivatedBookDao {
    @Query("SELECT * FROM activated_book")
    fun getAllActivatedBook(): Flow<List<ActivatedBookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg activatedBook: ActivatedBookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(activatedBook: List<ActivatedBookEntity>)
}