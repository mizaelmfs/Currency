package com.example.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.currency.dao.entiry.QuoteResponseEntity

data class QuoteResponseWithQuote (
    @Embedded var quoteResponseEntity: QuoteResponseEntity,
    @Relation(parentColumn = "id", entityColumn = "quoteResponseEntityId", entity = QuoteEntity::class)
    var quoteEntities: List<QuoteEntity>? = null
)