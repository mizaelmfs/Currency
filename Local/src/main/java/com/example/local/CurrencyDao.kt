package com.example.local

import androidx.room.Dao
import androidx.room.Query
import com.example.local.entity.CurrencyEntity

@Dao
interface CurrencyDao: GenericDao<CurrencyEntity> {

    @Query("SELECT * FROM CurrencyEntity")
    suspend fun getCurrency() : List<CurrencyEntity>

    @Query("SELECT * FROM CurrencyEntity WHERE `key` LIKE :value OR value LIKE :value")
    suspend fun getCurrency(value: String) : List<CurrencyEntity>

}