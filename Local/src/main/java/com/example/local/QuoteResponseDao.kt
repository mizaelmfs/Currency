package com.example.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.currency.dao.entiry.QuoteResponseEntity
import com.example.local.entity.QuoteResponseWithQuote

@Dao
interface QuoteResponseDao: GenericDao<QuoteResponseEntity> {

    @Transaction
    @Query("SELECT * FROM QuoteResponseEntity")
    suspend fun allQuoteResponse(): QuoteResponseWithQuote?

    @Query("SELECT * FROM QuoteResponseEntity")
    suspend fun getQuoteResponse(): QuoteResponseEntity?
}