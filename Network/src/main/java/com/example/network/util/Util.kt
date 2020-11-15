package com.example.network.util

import com.example.network.model.response.ErrorResponse
import retrofit2.HttpException
import java.io.IOException
import com.example.network.config.Result
import com.example.network.model.Error
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

const val BASE_URL = "http://apilayer.net/api/"
const val ACCESS_KEY = "bed048402103ae253c356da3717dbcbc"

// Exceptions
class NoConnectivityException: IOException()

//Call of Service
suspend fun <T> callService(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): Result<T> {
    return withContext(dispatcher) {
        try {
            Result.Success(apiCall())
        } catch (throwable: Throwable) {
            when (throwable) {
                is NoConnectivityException -> Result.NetworkError
                is IOException -> Result.NetworkError
                is HttpException -> {
                    Result.GenericError(
                        ErrorResponse (
                            error = Error(
                                throwable.code(),
                                throwable.message()
                            )
                        )
                    )
                }
                else -> {
                    Result.GenericError(null)
                }
            }
        }
    }
}