package com.vungn.backvietlibrary.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vungn.backvietlibrary.db.entity.BookEntity

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAllBooks(): List<BookEntity>

    @Query("SELECT * FROM book where id = :id")
    fun getBookById(id: String): BookEntity

    @Insert
    fun insertBooks(vararg books: BookEntity)
}