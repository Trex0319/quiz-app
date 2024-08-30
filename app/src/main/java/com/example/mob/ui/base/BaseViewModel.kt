package com.example.mob.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob.core.service.AuthService
import com.example.mob.core.utils.UserRole
import com.example.mob.data.model.Quiz
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    protected val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    private val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())
    open val quizzes: StateFlow<List<Quiz>> = _quizzes

    open val finish: MutableSharedFlow<Unit> = MutableSharedFlow()

    protected val _isLoading = MutableStateFlow(false)
    open val isLoading: StateFlow<Boolean> = _isLoading

    protected val _success = MutableSharedFlow<UserRole>()
    val success: SharedFlow<UserRole> = _success

    suspend fun <T> errorHandler(function: suspend () -> T?): T? {
        return try {
            function()
        } catch (e: Exception) {
            _error.emit(e.message.toString())
            e.printStackTrace()
            null
        }
    }
}