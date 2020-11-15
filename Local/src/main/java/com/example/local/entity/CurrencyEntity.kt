package com.example.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.network.model.Currency
import java.io.Serializable

@Entity
data class CurrencyEntity (
    @ForeignKey(entity = CurrencyResponseEntity::class,
        parentColumns = ["id"],
        childColumns = ["currencyResponseEntityId"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )
    var currencyResponseEntityId: Long? = null,
    @PrimaryKey val key: String,
    val value: String
) : Serializable


fun Currency.toCurrencyEntity(): CurrencyEntity {
    return with(this) {
        CurrencyEntity(
            key = this.key,
            value = this.value
        )
    }
}

fun CurrencyEntity.toCurrency(): Currency {
    return with(this) {
        Currency(
            key = this.key,
            value = this.value
        )
    }
}