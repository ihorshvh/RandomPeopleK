package com.paint.randompeoplek.di

import com.paint.randompeoplek.errorhandler.ErrorHandler
import com.paint.randompeoplek.errorhandler.ErrorHandlerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ErrorHandlerModule {

    @Provides
    @Singleton
    fun provideErrorHandler() : ErrorHandler {
        return ErrorHandlerImpl()
    }

}