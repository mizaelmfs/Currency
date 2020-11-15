package com.example.network.service

import com.google.gson.JsonObject
import retrofit2.http.GET

interface ICurrencyService {

    @GET("list")
    suspend fun getListCurrency(): JsonObject
}