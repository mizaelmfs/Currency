package com.example.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.network.model.response.CurrencyResponse
import java.io.Serializable

@Entity
data class CurrencyResponseEntity (
    @PrimaryKey val id: Long = 1,
    val success: Boolean,
    val terms: String,
    val privacy: String
): Serializable

fun CurrencyResponse.toCurrencyResponseEntity(): CurrencyResponseEntity {
    return with(this) {
        CurrencyResponseEntity(
            privacy = this.privacy,
            success = this.success,
            terms = this.terms
        )
    }
}