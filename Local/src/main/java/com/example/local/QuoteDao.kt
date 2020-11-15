package com.example.local

import androidx.room.Dao
import androidx.room.Query
import com.example.local.entity.QuoteEntity

@Dao
interface QuoteDao: GenericDao<QuoteEntity> {

    @Query("SELECT * FROM QuoteEntity")
    suspend fun getQuote() : List<QuoteEntity>
}