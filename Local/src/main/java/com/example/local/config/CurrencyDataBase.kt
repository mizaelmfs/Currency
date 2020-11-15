package com.example.local.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.local.CurrencyDao
import com.example.local.CurrencyResponseDao
import com.example.local.QuoteDao
import com.example.local.QuoteResponseDao
import com.example.local.entity.CurrencyEntity
import com.example.local.entity.CurrencyResponseEntity
import com.example.local.entity.QuoteEntity
import com.example.currency.dao.entiry.QuoteResponseEntity

@Database(entities = [CurrencyResponseEntity::class,
    CurrencyEntity::class,
    QuoteResponseEntity::class,
    QuoteEntity::class],
    version = 1)
abstract class CurrencyDataBase : RoomDatabase() {

    abstract fun currencyResponseDao(): CurrencyResponseDao
    abstract fun currencyDao() : CurrencyDao
    abstract fun quoteResponseDao() : QuoteResponseDao
    abstract fun quoteDao() : QuoteDao

    companion object {
        @Volatile
        private var INSTANCE: CurrencyDataBase? = null
        fun invoke(context: Context) : CurrencyDataBase {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance =  Room.databaseBuilder(context.applicationContext,
                    CurrencyDataBase::class.java,
                    "currency.db")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}