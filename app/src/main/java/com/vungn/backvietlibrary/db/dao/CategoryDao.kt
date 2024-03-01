package com.vungn.backvietlibrary.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vungn.backvietlibrary.db.entity.CategoryEntity

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getAllCategories(): List<CategoryEntity>

    @Query("SELECT * FROM category where id = :id")
    fun getCategoryById(id: String): CategoryEntity

    @Query("SELECT bookIds FROM category where id = :id")
    fun getBookIdsByCategoryId(id: String): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categories: List<CategoryEntity>)
}