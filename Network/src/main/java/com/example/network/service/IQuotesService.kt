package com.example.network.service

import com.google.gson.JsonObject
import retrofit2.http.GET

interface IQuotesService {

    @GET("live")
    suspend fun getListQuotes(): JsonObject
}