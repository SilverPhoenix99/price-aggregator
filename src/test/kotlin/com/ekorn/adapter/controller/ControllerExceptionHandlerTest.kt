package com.ekorn.adapter.controller

import com.ekorn.adapter.controller.exception.NotFoundException
import com.ekorn.adapter.controller.model.ErrorResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ControllerExceptionHandlerTest {

    val subject = ControllerExceptionHandler()

    @Test
    fun `handleNotFoundException should return with custom message`() {

        // Given
        val exception = NotFoundException("Custom not found message")

        // When
        val actual = subject.handleNotFoundException(exception)

        // Then
        val expected = ErrorResponse(
            status = 404,
            reason = "Not Found",
            error = "Custom not found message"
        )

        assertThat(actual.body).isEqualTo(expected)
    }
}
