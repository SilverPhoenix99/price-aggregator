package com.ekorn.adapter.controller.model

data class ErrorResponse(
    val status: Int,
    val reason: String,
    val error: String
)
