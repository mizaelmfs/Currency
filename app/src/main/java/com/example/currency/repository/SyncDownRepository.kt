package com.example.currency.repository

import com.example.currency.dao.entiry.toQuoteResponseEntity
import com.example.local.config.ResultLocal
import com.example.local.entity.*
import com.example.network.config.Result

class SyncDownRepository(
    private val repQuotes: QuotesRepository,
    private val repCurrency: CurrencyRepository
) {

    private suspend fun loadCurrency() : Result<Unit> {
         when (val responseCurrency = repCurrency.getListCurrency()) {
            is Result.NetworkError -> return Result.NetworkError
            is Result.GenericError -> return Result.GenericError(responseCurrency.errorResponse)
            is Result.Success -> {
                repCurrency.saveCurrencyDB(responseCurrency.value.toCurrencyResponseEntity())
                when(val data = repCurrency.getCurrencyResponseEntityDB()) {
                    is ResultLocal.Success -> {
                        val currencyEntities = mutableListOf<CurrencyEntity>()
                        responseCurrency.value.currencies.forEach { currency ->
                            val currencyEntity = currency.toCurrencyEntity()
                            currencyEntity.currencyResponseEntityId = data.value?.id
                            currencyEntities.add(currencyEntity)
                        }
                        repCurrency.saveListCurrencyDB(currencyEntities)
                        return when(val resultQuotes = loadQuotes()) {
                            is Result.Success -> Result.Success(Unit)
                            is Result.NetworkError -> Result.NetworkError
                            is Result.GenericError -> Result.GenericError(resultQuotes.errorResponse)
                        }
                    }
                    is ResultLocal.Error -> {
                        return Result.NetworkError
                    }
                }
            }
        }
    }

    private suspend fun loadQuotes() : Result<Unit> {
        when (val responseQuote = repQuotes.getListQuotes()) {
            is Result.NetworkError -> return Result.NetworkError
            is Result.GenericError -> return Result.GenericError(responseQuote.errorResponse)
            is Result.Success -> {
                repQuotes.saveQuoteDB(responseQuote.value.toQuoteResponseEntity())
                return when(val data = repQuotes.getQuoteResponseEntityDB()) {
                    is ResultLocal.Success -> {
                        val quoteEntities = mutableListOf<QuoteEntity>()
                        responseQuote.value.quotes.forEach { quote ->
                            val quoteEntity = quote.toQuoteEntity()
                            quoteEntity.quoteResponseEntityId = data.value?.id
                            quoteEntities.add(quoteEntity)
                        }
                        repQuotes.saveListQuoteDB(quoteEntities)
                        Result.Success(Unit)
                    }
                    is ResultLocal.Error -> {
                        Result.NetworkError
                    }
                }
            }
        }
    }

    suspend fun startSyncDown(): Result<Unit> {
        return loadCurrency()
    }
}

