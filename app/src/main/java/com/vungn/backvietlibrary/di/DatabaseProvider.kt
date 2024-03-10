package com.vungn.backvietlibrary.di

import android.content.Context
import androidx.room.Room
import com.vungn.backvietlibrary.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseProvider {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        val db = Room.databaseBuilder(context, AppDatabase::class.java, "backvietlibrary.db")
        db.setQueryCallback({ sqlQuery, bindArgs ->
            println("SQL Query: $sqlQuery SQL Args: $bindArgs")
        }, Executors.newSingleThreadExecutor())
        return db.build()
    }

    @Provides
    @Singleton
    fun provideBookDao(database: AppDatabase) = database.bookDao()

    @Provides
    @Singleton
    fun provideCategoryDao(database: AppDatabase) = database.categoryDao()

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase) = database.userDao()

    @Provides
    @Singleton
    fun provideBorrowDao(database: AppDatabase) = database.borrowDao()

    @Provides
    @Singleton
    fun provideBorrowDetailDao(database: AppDatabase) = database.borrowDetailDao()

    @Provides
    @Singleton
    fun provideCartDao(database: AppDatabase) = database.cartDao()

    @Provides
    @Singleton
    fun provideMediaDao(database: AppDatabase) = database.mediaDao()
}