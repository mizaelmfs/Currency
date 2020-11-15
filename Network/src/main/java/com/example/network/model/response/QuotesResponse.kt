package com.example.network.model.response

import com.example.network.model.Quote

data class QuotesResponse (
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val timestamp: Long,
    val source: String,
    val quotes: List<Quote>
)