package com.vungn.backvietlibrary.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vungn.backvietlibrary.db.dao.ActivatedBookDao
import com.vungn.backvietlibrary.db.dao.BookDao
import com.vungn.backvietlibrary.db.dao.BorrowDao
import com.vungn.backvietlibrary.db.dao.BorrowDetailDao
import com.vungn.backvietlibrary.db.dao.CartDao
import com.vungn.backvietlibrary.db.dao.CategoryDao
import com.vungn.backvietlibrary.db.dao.MediaDao
import com.vungn.backvietlibrary.db.dao.UserDao
import com.vungn.backvietlibrary.db.entity.ActivatedBookEntity
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.db.entity.BorrowDetailEntity
import com.vungn.backvietlibrary.db.entity.BorrowEntity
import com.vungn.backvietlibrary.db.entity.CartEntity
import com.vungn.backvietlibrary.db.entity.CategoryEntity
import com.vungn.backvietlibrary.db.entity.MediaEntity
import com.vungn.backvietlibrary.db.entity.UserEntity

@Database(
    entities = [
        BookEntity::class,
        CategoryEntity::class,
        UserEntity::class,
        BorrowEntity::class,
        BorrowDetailEntity::class,
        CartEntity::class,
        MediaEntity::class,
        ActivatedBookEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    abstract fun categoryDao(): CategoryDao

    abstract fun userDao(): UserDao

    abstract fun borrowDao(): BorrowDao

    abstract fun borrowDetailDao(): BorrowDetailDao

    abstract fun cartDao(): CartDao

    abstract fun mediaDao(): MediaDao

    abstract fun activatedBookDao(): ActivatedBookDao
}