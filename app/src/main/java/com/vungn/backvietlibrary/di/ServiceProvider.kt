package com.vungn.backvietlibrary.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.vungn.backvietlibrary.BuildConfig
import com.vungn.backvietlibrary.model.service.BookService
import com.vungn.backvietlibrary.model.service.BorrowService
import com.vungn.backvietlibrary.model.service.UserService
import com.vungn.backvietlibrary.network.NetworkEventInterceptor
import com.vungn.backvietlibrary.network.SuspendInterceptor
import com.vungn.backvietlibrary.network.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ServiceProvider {

    @Provides
    @Singleton
    fun retrofitProvider(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().client(okHttpClient).baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun okHttpClientProvider(
        tokenAuthenticator: TokenAuthenticator,
        dataStore: DataStore<Preferences>,
        networkEventInterceptor: NetworkEventInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(SuspendInterceptor(dataStore))
            .authenticator(tokenAuthenticator).addInterceptor(networkEventInterceptor)
            .addInterceptor(loggingInterceptor).build()
    }

    @Provides
    @Singleton
    fun userServiceProvider(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun bookServiceProvider(retrofit: Retrofit): BookService {
        return retrofit.create(BookService::class.java)
    }

    @Provides
    @Singleton
    fun borrowServiceProvider(retrofit: Retrofit): BorrowService {
        return retrofit.create(BorrowService::class.java)
    }
}