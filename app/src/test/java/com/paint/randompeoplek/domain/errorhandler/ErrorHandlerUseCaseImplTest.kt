package com.paint.randompeoplek.domain.errorhandler

import io.mockk.*
import org.junit.Test
import org.junit.Assert.*
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
        val response = mockkClass(Response::class)

        every {
            response.code()
        } returns 400

        every {
            response.message()
        } returns "Service Unavailable"

        val errorEntity = errorHandlerUseCase.getErrorEntity(HttpException(response))
        assertEquals(ErrorEntity.ServiceUnavailable, errorEntity)
    }

    @Test
    fun testGetErrorEntityWhenOtherException() {
        val errorEntity = errorHandlerUseCase.getErrorEntity(ArithmeticException())
        assertEquals(ErrorEntity.Unknown, errorEntity)
    }

}