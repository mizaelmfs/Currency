package com.example.currency.ui.listCurrency

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.model.response.CurrencyResponse
import com.example.network.config.Result
import com.example.local.config.ResultLocal
import com.example.currency.repository.CurrencyRepository
import kotlinx.coroutines.launch

class ListCurrencyViewModel(
    val repository: CurrencyRepository
) : ViewModel() {

    val responseCurrency = MutableLiveData<CurrencyResponse>()
    val errorMsg = MutableLiveData<String>()
    val listEmpty = MutableLiveData(false)
    val progress = MutableLiveData(true)

    fun getSearchData(value: String? = null) {
        if (value.isNullOrEmpty()) {
            viewModelScope.launch {
                getDataLocal()
            }
        } else {
            viewModelScope.launch {
                getSearchDataLocal(value)
            }
        }
    }

    private suspend fun getDataLocal() {
         when(val data = repository.getListCurrencyLocal()) {
            is ResultLocal.Success -> {
                progress.value = (false)
                listEmpty.value = (false)
                responseCurrency.value = data.value
            }
            is ResultLocal.Error -> {
                progress.value = (false)
                listEmpty.value = (true)
            }
        }
    }

    private suspend fun getSearchDataLocal(value: String? = null) {
        when(val data = repository.getListCurrencyLocal("${value}%")) {
            is ResultLocal.Success -> {
                progress.value = (false)
                listEmpty.value = (false)
                responseCurrency.value = data.value
            }
            is ResultLocal.Error -> {
                progress.value = (false)
                listEmpty.value = (true)
            }
        }
    }
}