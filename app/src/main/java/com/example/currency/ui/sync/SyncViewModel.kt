package com.example.currency.ui.sync

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.currency.R
import com.example.network.config.Result
import com.example.currency.repository.SyncDownRepository
import kotlinx.coroutines.launch

class SyncViewModel(
    val repository: SyncDownRepository,
    val navController: NavController
) : ViewModel() {

    val errorMsg = MutableLiveData<String>()
    val notNetwork = MutableLiveData<Unit>()

    fun start() {
        viewModelScope.launch {
            when (val success = repository.startSyncDown()) {
                is Result.NetworkError -> {
                    notNetwork.value = Unit
                    goToConversionScreen()
                }
                is Result.GenericError-> errorMsg.value = success.errorResponse?.error?.info!!
                is Result.Success -> {
                    goToConversionScreen()
                }
            }
        }
    }

    private fun goToConversionScreen() {
        navController.popBackStack(R.id.navigation_settings, true)
        navController.navigate(R.id.navigation_conversion)
    }

}