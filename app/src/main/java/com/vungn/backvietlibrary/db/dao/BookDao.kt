package com.vungn.backvietlibrary.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vungn.backvietlibrary.db.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM book ORDER BY publishYear DESC LIMIT 10")
    fun get10EarliestPublishedBook(): Flow<List<BookEntity>>

    @Query("SELECT * FROM book where id = :id")
    fun getBookById(id: String): Flow<BookEntity>

    @Query("SELECT * FROM book where id IN (:ids)")
    fun getBooksByIds(ids: List<String>): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg books: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(books: List<BookEntity>)

    @Query("DELETE FROM book")
    fun clear()
}