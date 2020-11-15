package com.example.network.config

import com.example.network.model.response.ErrorResponse

sealed class Result<out T> {
    data class Success<out T>(val value: T): Result<T>()
    data class GenericError(val errorResponse: ErrorResponse? = null): Result<Nothing>()
    object NetworkError: Result<Nothing>()
}