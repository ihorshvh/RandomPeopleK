package com.paint.randompeoplek.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.paint.randompeoplek.service.RandomPeopleService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://randomuser.me/api/1.3/"

@InstallIn(SingletonComponent::class)
@Module
object RandomPeopleServiceModule {

    @Provides
    @Singleton
    fun provideGson() : Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson : Gson) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideRandomPeopleService(retrofit: Retrofit) : RandomPeopleService {
        return retrofit.create(RandomPeopleService::class.java) as RandomPeopleService
    }

}