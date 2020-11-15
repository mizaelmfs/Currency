package com.example.currency.ui.conversion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.model.response.QuotesResponse
import com.example.local.config.ResultLocal
import com.example.currency.repository.QuotesRepository
import kotlinx.coroutines.launch

class ConversionViewModel(val repository: QuotesRepository) : ViewModel() {

    val txtResult = MutableLiveData<String>()
    val txtCalc = MutableLiveData<String>()
    val errorMsg = MutableLiveData<String>()
    val responseQuotes = MutableLiveData<QuotesResponse>()

    fun getData() {
        viewModelScope.launch {
            getDataLocal()
        }
    }

    private suspend fun getDataLocal() {
        when(val data = repository.getListQuoteLocal()) {
            is ResultLocal.Success -> {
                responseQuotes.value = data.value
            }
            is ResultLocal.Error -> {
                errorMsg.value = data.errorMsg
            }
        }
    }

     fun calcQuote(keyFrom: String?, keyTo:String?, value: Double? = null) {
        if (!keyFrom.isNullOrEmpty() && !keyTo.isNullOrEmpty()) {
            val valueFrom = searchKeys(keyFrom)
            val valueTo = searchKeys(keyTo)

            var valueFromDollar = 0.0
            if (value != null) {
                valueFromDollar = convertInDollar(value, valueFrom)
            }
            txtCalc.value = (valueFromDollar * valueTo).toString()
        }
     }

     private fun searchKeys(value: String): Double {
        responseQuotes.value?.quotes?.first {
            it.key.substring(3, 6) == value
        }.also {
            txtResult.value = it?.value.toString()
            return it?.value!!
        }
    }

    private fun convertInDollar(origin : Double, destine: Double) : Double {
        return origin.div(destine)
    }

}