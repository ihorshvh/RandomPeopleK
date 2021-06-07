package com.paint.randompeoplek.errorhandler

import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito.mock
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.ArithmeticException

class ErrorHandlerImplTest {

    private val errorHandler : ErrorHandler = ErrorHandlerImpl()

    @Test
    fun testGetErrorEntityWhenIOException() {
        val errorEntity = errorHandler.getErrorEntity(IOException())
        assertEquals(ErrorEntity.Network, errorEntity)
    }

    @Test
    fun testGetErrorEntityWhenHttpException() {
        val response = mock(Response::class.java)
        val errorEntity = errorHandler.getErrorEntity(HttpException(response))
        assertEquals(ErrorEntity.ServiceUnavailable, errorEntity)
    }

    @Test
    fun testGetErrorEntityWhenOtherException() {
        val errorEntity = errorHandler.getErrorEntity(ArithmeticException())
        assertEquals(ErrorEntity.Unknown, errorEntity)
    }

}