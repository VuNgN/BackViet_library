package com.vungn.backvietlibrary.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object JsonProvider {

    @Provides
    @Singleton
    fun gsonProvider(): Gson {
        return GsonBuilder().setLenient().create()
    }
}