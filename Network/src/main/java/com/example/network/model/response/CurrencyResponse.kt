package com.example.network.model.response

import com.example.network.model.Currency

data class CurrencyResponse (
    val success: Boolean,
    val terms: String,
    val privacy: String,
    var currencies: List<Currency>
)