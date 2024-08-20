package com.example.mob.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseViewModel: ViewModel() {
    protected val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    suspend fun <T>errorHandler(func: suspend () -> T?): T? {
        return try {
            func()
        } catch (e: Exception) {
            _error.emit(e.message.toString())
            e.printStackTrace()
            null
        }
    }

}