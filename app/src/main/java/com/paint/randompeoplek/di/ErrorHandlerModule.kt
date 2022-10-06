package com.paint.randompeoplek.di

import com.paint.randompeoplek.errorhandler.ErrorHandlerUseCase
import com.paint.randompeoplek.errorhandler.ErrorHandlerUseCaseImpl
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
    fun provideErrorHandler() : ErrorHandlerUseCase {
        return ErrorHandlerUseCaseImpl()
    }

}