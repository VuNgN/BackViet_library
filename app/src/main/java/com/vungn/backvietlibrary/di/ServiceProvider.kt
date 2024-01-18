package com.vungn.backvietlibrary.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.vungn.backvietlibrary.BuildConfig
import com.vungn.backvietlibrary.model.service.UserService
import com.vungn.backvietlibrary.network.SuspendInterceptor
import com.vungn.backvietlibrary.network.TokenAuthenticator
import com.vungn.backvietlibrary.util.key.PreferenceKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import okhttp3.OkHttpClient
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
        tokenAuthenticator: TokenAuthenticator, dataStore: DataStore<Preferences>
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(SuspendInterceptor(dataStore))
            .authenticator(tokenAuthenticator).build()
    }

    @Provides
    @Singleton
    fun userServiceProvider(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
}