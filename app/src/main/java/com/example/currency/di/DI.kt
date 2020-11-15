package com.example.currency.di

import androidx.navigation.NavController
import com.example.currency.repository.CurrencyRepository
import com.example.currency.repository.QuotesRepository
import com.example.currency.repository.SyncDownRepository
import com.example.currency.ui.conversion.ConversionViewModel
import com.example.currency.ui.listCurrency.ListCurrencyViewModel
import com.example.currency.ui.sync.SyncViewModel
import com.example.network.config.ConnectivityInterceptor
import com.example.network.config.ServiceConfig
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val coreModule = module {
    single {
        ConnectivityInterceptor(
            context = get()
        )
    }
    single {
        ServiceConfig(
            connectivityInterceptor = get()
        )
    }
}

val repositoryModule = module {
    factory { CurrencyRepository(context = androidContext(), service = get()) }
    factory { QuotesRepository(context = androidContext(), service = get()) }
    factory { SyncDownRepository(repCurrency = get(), repQuotes = get()) }
}

val viewModelModule = module {
    viewModel {
        ListCurrencyViewModel(repository = get())
    }

    viewModel {
        ConversionViewModel(repository = get())
    }

    viewModel { (navController: NavController) ->
        SyncViewModel(repository = get(), navController = navController)
    }
}