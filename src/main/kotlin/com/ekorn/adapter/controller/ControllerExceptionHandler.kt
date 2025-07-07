package com.ekorn.adapter.controller

import com.ekorn.adapter.controller.exception.NotFoundException
import com.ekorn.adapter.controller.model.ErrorResponse
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(exception: NotFoundException): ResponseEntity<ErrorResponse> {

        return ResponseEntity.status(NOT_FOUND.value())
            .body(ErrorResponse(
                NOT_FOUND.value(),
                NOT_FOUND.reasonPhrase,
                exception.message ?: "Resource not found"
            ))
    }
}
