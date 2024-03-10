package com.vungn.backvietlibrary.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vungn.backvietlibrary.db.entity.MediaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {
    @Query("SELECT * FROM media WHERE bookId = :bookId")
    fun getMediaByBookId(bookId: String): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media WHERE bookId = :bookId AND isMain = 1")
    fun getMainMediaByBookId(bookId: String): Flow<MediaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg media: MediaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(media: List<MediaEntity>)

    @Query("DELETE FROM media")
    fun clear()
}