package com.example.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CurrencyResponseWithCurrency (
    @Embedded var currencyResponseEntity: CurrencyResponseEntity,
    @Relation(parentColumn = "id", entityColumn = "currencyResponseEntityId", entity = CurrencyEntity::class)
    var currencyEntities: List<CurrencyEntity>? = null
)