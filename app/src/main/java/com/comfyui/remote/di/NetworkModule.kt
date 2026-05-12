package com.comfyui.remote.di

import com.comfyui.remote.data.api.ComfyUIApi
import com.comfyui.remote.data.local.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        preferencesManager: PreferencesManager
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val apiKey = runBlocking { preferencesManager.apiKey.first() }
                val request = if (apiKey.isNotEmpty()) {
                    chain.request().newBuilder()
                        .addHeader("api_key", apiKey)
                        .build()
                } else {
                    chain.request()
                }
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        preferencesManager: PreferencesManager
    ): Retrofit {
        val serverUrl = runBlocking { preferencesManager.serverUrl.first() }
        val baseUrl = if (serverUrl.isNotEmpty()) {
            serverUrl.ensureEndsWith("/")
        } else {
            "http://localhost:8188/"
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideComfyUIApi(retrofit: Retrofit): ComfyUIApi {
        return retrofit.create(ComfyUIApi::class.java)
    }
}

private fun String.ensureEndsWith(path: String = "/"): String {
    return if (this.endsWith(path)) this else "$this$path"
}
