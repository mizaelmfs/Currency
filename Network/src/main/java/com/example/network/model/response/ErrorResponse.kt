package com.example.network.model.response

import com.example.network.model.Error

data class ErrorResponse (
    val success: Boolean = false,
    val error: Error
)