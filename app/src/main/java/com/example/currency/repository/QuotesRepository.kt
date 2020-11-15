package com.example.currency.repository

import android.content.Context
import com.example.local.config.CurrencyDataBase
import com.example.network.model.response.QuotesResponse
import com.example.network.config.Result
import com.example.local.config.ResultLocal
import com.example.local.entity.QuoteEntity
import com.example.network.config.ServiceConfig
import com.example.currency.util.callLocal
import com.example.currency.dao.entiry.QuoteResponseEntity
import com.example.local.entity.QuoteResponseWithQuote
import com.example.local.entity.toQuote
import com.example.network.util.callService
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject

class QuotesRepository(
    private val service: ServiceConfig,
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private suspend fun getListQuotesRemote(): Result<JsonObject> {
        return callService(dispatcher) { service.quotesService?.getListQuotes()!! }
    }

    suspend fun getListQuotes() : Result<QuotesResponse> {
        return when (val response = getListQuotesRemote()) {
            is Result.NetworkError -> Result.NetworkError
            is Result.GenericError -> Result.GenericError(response.errorResponse)
            is Result.Success -> {
                try {
                    val jsonObject = JSONObject(response.value.toString())
                    val success = jsonObject.getBoolean("success")
                    val terms = jsonObject.getString("terms")
                    val privacy = jsonObject.getString("privacy")
                    val timestamp = jsonObject.getLong("timestamp")
                    val source = jsonObject.getString("source")
                    val quotes = mutableListOf<com.example.network.model.Quote>()
                    val jsonObjectQuotes = jsonObject.getJSONObject("quotes")
                    val keys = jsonObjectQuotes.keys()
                    while (keys.hasNext()) {
                        val key = keys.next() as String
                        quotes.add(
                            com.example.network.model.Quote(
                                key,
                                jsonObjectQuotes.getDouble(key)
                            )
                        )
                    }
                    Result.Success(QuotesResponse(success, terms, privacy, timestamp, source, quotes))
                } catch (e: Exception) {
                    Result.NetworkError
                }
            }
        }
    }

    private suspend fun getListQuoteDB(): ResultLocal<QuoteResponseWithQuote?> {
        return callLocal(dispatcher) { CurrencyDataBase.invoke(context).quoteResponseDao().allQuoteResponse() }
    }

    suspend fun getListQuoteLocal() : ResultLocal<QuotesResponse> {
        when(val data = getListQuoteDB()) {
            is ResultLocal.Error -> return ResultLocal.Error(data.errorMsg)
            is ResultLocal.Success -> {
                data.value?.also {
                    val quotes = mutableListOf<com.example.network.model.Quote>()
                    it.quoteEntities?.forEach { quoteEntity ->
                        quotes.add(quoteEntity.toQuote())
                    }
                    return ResultLocal.Success(
                        QuotesResponse(
                        success = it.quoteResponseEntity.success,
                        privacy = it.quoteResponseEntity.privacy,
                        terms = it.quoteResponseEntity.terms,
                        quotes = quotes,
                        source = it.quoteResponseEntity.source,
                        timestamp = it.quoteResponseEntity.timestamp
                        )
                    )
                }
                return ResultLocal.Error(null)
            }
        }
    }

    suspend fun saveQuoteDB(value : QuoteResponseEntity): ResultLocal<Unit> {
        return callLocal(dispatcher) { CurrencyDataBase.invoke(context).quoteResponseDao().save(value) }
    }

    suspend fun saveListQuoteDB(value : List<QuoteEntity>): ResultLocal<Unit> {
        return callLocal(dispatcher) { CurrencyDataBase.invoke(context).quoteDao().save(value) }
    }

    suspend fun getQuoteResponseEntityDB(): ResultLocal<QuoteResponseEntity?> {
        return callLocal(dispatcher) { CurrencyDataBase.invoke(context).quoteResponseDao().getQuoteResponse() }
    }
}