package com.example.local.config

sealed class ResultLocal<out T> {
    data class Success<out T>(val value: T? = null): ResultLocal<T>()
    data class Error(val errorMsg: String? = null): ResultLocal<Nothing>()
}