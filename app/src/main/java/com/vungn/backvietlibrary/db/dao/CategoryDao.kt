package com.vungn.backvietlibrary.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vungn.backvietlibrary.db.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM category where id = :id")
    fun getCategoryById(id: String): Flow<CategoryEntity>

    @Query("SELECT bookIds FROM category where id = :id")
    fun getBookIdsByCategoryId(id: String): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categories: List<CategoryEntity>)
}