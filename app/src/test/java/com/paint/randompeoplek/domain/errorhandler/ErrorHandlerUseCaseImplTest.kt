package com.paint.randompeoplek.domain.errorhandler

import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito.mock
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.ArithmeticException

class ErrorHandlerUseCaseImplTest {

    private val errorHandlerUseCase : ErrorHandlerUseCase = ErrorHandlerUseCaseImpl()

    @Test
    fun testGetErrorEntityWhenIOException() {
        val errorEntity = errorHandlerUseCase.getErrorEntity(IOException())
        assertEquals(ErrorEntity.Network, errorEntity)
    }

    @Test
    fun testGetErrorEntityWhenHttpException() {
        val response = mock(Response::class.java)
        val errorEntity = errorHandlerUseCase.getErrorEntity(HttpException(response))
        assertEquals(ErrorEntity.ServiceUnavailable, errorEntity)
    }

    @Test
    fun testGetErrorEntityWhenOtherException() {
        val errorEntity = errorHandlerUseCase.getErrorEntity(ArithmeticException())
        assertEquals(ErrorEntity.Unknown, errorEntity)
    }

}