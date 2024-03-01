package com.vungn.backvietlibrary.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vungn.backvietlibrary.db.dao.BookDao
import com.vungn.backvietlibrary.db.dao.CategoryDao
import com.vungn.backvietlibrary.db.dao.UserDao
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.db.entity.CategoryEntity
import com.vungn.backvietlibrary.db.entity.UserEntity

@Database(entities = [BookEntity::class, CategoryEntity::class, UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    abstract fun categoryDao(): CategoryDao

    abstract fun userDao(): UserDao
}