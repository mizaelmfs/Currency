package com.example.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.local.entity.CurrencyResponseEntity
import com.example.local.entity.CurrencyResponseWithCurrency

@Dao
interface CurrencyResponseDao: GenericDao<CurrencyResponseEntity> {

    @Transaction
    @Query("SELECT * FROM CurrencyResponseEntity")
    suspend fun allCurrencyResponse(): CurrencyResponseWithCurrency?

    @Query("SELECT * FROM CurrencyResponseEntity")
    suspend fun getCurrencyResponse(): CurrencyResponseEntity?
}