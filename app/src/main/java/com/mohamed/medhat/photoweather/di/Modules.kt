package com.mohamed.medhat.photoweather.di

import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mohamed.medhat.photoweather.networking.WeatherAuthenticator
import com.mohamed.medhat.photoweather.networking.WebApi
import com.mohamed.medhat.photoweather.repository.MainRepository
import com.mohamed.medhat.photoweather.repository.Repository
import com.mohamed.medhat.photoweather.utils.Constants
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Tells hilt how to provide some dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object Providers {

    @Provides
    fun provideLocationManager(@ApplicationContext context: Context): LocationManager {
        return context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @Provides
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(WeatherAuthenticator())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideWebApi(retrofit: Retrofit): WebApi {
        return retrofit.create(WebApi::class.java)
    }
}

/**
 * Tells hilt how to provide some dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class Binders {
    @MainRepo
    @Binds
    abstract fun bindRepository(mainRepository: MainRepository): Repository
}

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class MainRepo