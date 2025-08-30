package com.paint.randompeoplek.di

import android.content.Context
import com.paint.randompeoplek.domain.errorhandler.ErrorHandlerUseCase
import com.paint.randompeoplek.domain.errorhandler.ErrorHandlerUseCaseImpl
import com.paint.randompeoplek.resourceprovider.AndroidResourceProvider
import com.paint.randompeoplek.resourceprovider.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ErrorHandlerModule {

    @Provides
    @Singleton
    fun provideResourceProvider(@ApplicationContext applicationContext: Context) : ResourceProvider {
        return AndroidResourceProvider(applicationContext)
    }

    @Provides
    @Singleton
    fun provideErrorHandler(resourceProvider: ResourceProvider) : ErrorHandlerUseCase {
        return ErrorHandlerUseCaseImpl(resourceProvider)
    }
}