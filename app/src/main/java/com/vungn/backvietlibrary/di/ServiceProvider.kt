package com.vungn.backvietlibrary.di

import com.vungn.backvietlibrary.model.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ServiceProvider {

    @Provides
    @Singleton
    fun retrofitProvider(): Retrofit {
        val httpClient = OkHttpClient.Builder()
        return Retrofit.Builder()
            .baseUrl("https://test.thuvien247.vn/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    @Provides
    @Singleton
    fun userServiceProvider(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
}