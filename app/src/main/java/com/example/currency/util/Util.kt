package com.example.currency.util

import com.example.local.config.ResultLocal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

//Call of Local
suspend fun <T> callLocal(dispatcher: CoroutineDispatcher, localCall: suspend () -> T): ResultLocal<T> {
    return withContext(dispatcher) {
        try {
            ResultLocal.Success(localCall())
        } catch (throwable: Throwable) {
            ResultLocal.Error(throwable.message)
        }
    }
}

fun getDate(timestamp: Long) : String {
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = timestamp * 1000L
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return simpleDateFormat.format(calendar.time)
}