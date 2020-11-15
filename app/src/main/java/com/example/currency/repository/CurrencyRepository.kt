package com.example.currency.repository

import android.content.Context
import com.example.local.entity.CurrencyEntity
import com.example.local.entity.CurrencyResponseEntity
import com.example.local.entity.CurrencyResponseWithCurrency
import com.example.local.entity.toCurrency
import com.example.currency.util.callLocal
import com.example.local.config.CurrencyDataBase
import com.example.local.config.ResultLocal
import com.example.network.config.ServiceConfig
import com.example.network.model.Currency
import com.example.network.model.response.CurrencyResponse
import com.example.network.util.callService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject
import com.example.network.config.Result
import com.google.gson.JsonObject

class CurrencyRepository(
    private val service: ServiceConfig,
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private suspend fun getListCurrencyRemote(): Result<JsonObject> {
        return callService(dispatcher) { service.currencyService?.getListCurrency()!! }
    }

    suspend fun getListCurrency() : Result<CurrencyResponse> {
         when (val response = getListCurrencyRemote()) {
            is Result.NetworkError -> return Result.NetworkError
            is Result.GenericError -> return Result.GenericError(response.errorResponse)
            is Result.Success -> {
                try {
                    val jsonObject = JSONObject(response.value.toString())
                    val success = jsonObject.getBoolean("success")
                    val terms = jsonObject.getString("terms")
                    val privacy = jsonObject.getString("privacy")
                    val currencies = mutableListOf<Currency>()
                    val jsonObjectCurrencies = jsonObject.getJSONObject("currencies")
                    val keys = jsonObjectCurrencies.keys()
                    while (keys.hasNext()) {
                        val key = keys.next() as String
                        currencies.add(
                            Currency(
                                key = key,
                                value = jsonObjectCurrencies.get(key).toString()
                            )
                        )
                    }
                    return Result.Success(CurrencyResponse(success, terms, privacy, currencies))
                } catch (e: Exception) {
                    return Result.NetworkError
                }
            }
        }
    }

    suspend fun saveCurrencyDB(value : CurrencyResponseEntity): ResultLocal<Unit> {
        return callLocal(dispatcher) { CurrencyDataBase.invoke(context).currencyResponseDao().save(value) }
    }
    suspend fun saveListCurrencyDB(value : List<CurrencyEntity>): ResultLocal<Unit> {
        return callLocal(dispatcher) { CurrencyDataBase.invoke(context).currencyDao().save(value) }
    }

     suspend fun getCurrencyResponseEntityDB(): ResultLocal<CurrencyResponseEntity?> {
        return callLocal(dispatcher) { CurrencyDataBase.invoke(context).currencyResponseDao().getCurrencyResponse() }
    }

    private suspend fun getListCurrencyDB(): ResultLocal<CurrencyResponseWithCurrency?> {
        return callLocal(dispatcher) { CurrencyDataBase.invoke(context).currencyResponseDao().allCurrencyResponse() }
    }

    private suspend fun getListCurrencyDB(value: String? = null): ResultLocal<List<CurrencyEntity>?> {
        return callLocal(dispatcher) { CurrencyDataBase.invoke(context).currencyDao().getCurrency(value!!) }
    }

     suspend fun getListCurrencyLocal(value: String? = null) : ResultLocal<CurrencyResponse> {
           when(val data = getListCurrencyDB(value)) {
              is ResultLocal.Error -> return ResultLocal.Error(data.errorMsg)
              is ResultLocal.Success -> {
                 data.value?.also {
                     val currencies = mutableListOf<Currency>()
                     it.forEach { CurrencyEntity ->
                         currencies.add(CurrencyEntity.toCurrency())
                     }
                     return ResultLocal.Success(CurrencyResponse(
                         success = true,
                         privacy = "",
                         terms = "",
                         currencies = currencies
                     ))
                 }
                  return ResultLocal.Error(null)
             }
         }
    }

    suspend fun getListCurrencyLocal() : ResultLocal<CurrencyResponse> {
        when(val data = getListCurrencyDB()) {
            is ResultLocal.Error -> return ResultLocal.Error(data.errorMsg)
            is ResultLocal.Success -> {
                data.value?.also {
                    val currencies = mutableListOf<Currency>()
                    it.currencyEntities?.forEach { CurrencyEntity ->
                        currencies.add(CurrencyEntity.toCurrency())
                    }
                    return ResultLocal.Success(CurrencyResponse(
                        success = it.currencyResponseEntity.success,
                        privacy = it.currencyResponseEntity.privacy,
                        terms = it.currencyResponseEntity.terms,
                        currencies = currencies
                    ))
                }
                return ResultLocal.Error(null)
            }
        }
    }

}