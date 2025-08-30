package com.paint.randompeoplek.domain.errorhandler

import io.mockk.*
import org.junit.Test
import org.junit.Assert.*
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.ArithmeticException

class ErrorHandlerUseCaseImplTest {
// TODO fix tests
//    private val errorHandlerUseCase : ErrorHandlerUseCase = ErrorHandlerUseCaseImpl()
//
//    @Test
//    fun testGetErrorMessageWhenIOException() {
//        val errorEntity = errorHandlerUseCase.getErrorMessage(IOException())
//        //assertEquals(ErrorEntity.Network, errorEntity)
//    }
//
//    @Test
//    fun testGetErrorMessageWhenHttpException() {
//        val response = mockkClass(Response::class)
//
//        every {
//            response.code()
//        } returns 400
//
//        every {
//            response.message()
//        } returns "Service Unavailable"
//
//        val errorEntity = errorHandlerUseCase.getErrorMessage(HttpException(response))
//        assertEquals(ErrorEntity.ServiceUnavailable, errorEntity)
//    }
//
//    @Test
//    fun testGetErrorMessageWhenOtherException() {
//        val errorEntity = errorHandlerUseCase.getErrorMessage(ArithmeticException())
//        assertEquals(ErrorEntity.Unknown, errorEntity)
//    }

}