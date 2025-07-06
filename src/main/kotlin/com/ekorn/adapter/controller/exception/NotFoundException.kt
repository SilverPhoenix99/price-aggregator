package com.ekorn.adapter.controller.exception

import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = NOT_FOUND)
class NotFoundException(message: String) : RuntimeException(message)
