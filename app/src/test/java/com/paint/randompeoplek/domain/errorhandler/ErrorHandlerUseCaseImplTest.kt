package com.paint.randompeoplek.domain.errorhandler

import com.paint.randompeoplek.repository.NetworkError
import com.paint.randompeoplek.resourceprovider.ResourceProvider
import io.mockk.mockk
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.IOException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ErrorHandlerUseCaseImplTest {

    private lateinit var errorHandlerUseCase : ErrorHandlerUseCase
    private lateinit var resourceProvider: ResourceProvider

    @BeforeEach
    fun setUp() {
        resourceProvider = mockk()
        errorHandlerUseCase = ErrorHandlerUseCaseImpl(resourceProvider)
    }

    @Test
    fun testGetErrorMessageWhenException() {
        every { resourceProvider.getString(any()) } returns "Unknown error"
        val errorMessage = errorHandlerUseCase.getErrorMessage(Exception())
        assertEquals("Unknown error", errorMessage)
    }

    @ParameterizedTest
    @CsvSource(
        "REQUEST_TIMEOUT, Timeout error",
        "UNAUTHORIZED, Unauthorized error",
        "NO_INTERNET, No internet error",
        "SERVER_ERROR, Server error",
        "UNKNOWN, Unknown error"
    )
    fun testGetErrorMessageWhenNetworkError(networkError: NetworkError, expectedMessage: String) {
        every { resourceProvider.getString(any()) } returns expectedMessage
        val errorMessage = errorHandlerUseCase.getErrorMessage(networkError)
        assertEquals(expectedMessage, errorMessage)
    }
}