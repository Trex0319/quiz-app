package com.example.mob.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mob.core.service.AuthService
import com.example.mob.data.repo.UserRepo
import com.example.mob.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class LoginViewModel @Inject constructor (
    private val authService: AuthService,
    private val userRepo: UserRepo,
) : BaseViewModel() {

//    val success: MutableSharedFlow<Unit> = MutableSharedFlow()
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun login(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            errorHandler {
                validate(email, pass)
                authService.loginWithEmailAndPass(email, pass)

            }?.let {
                val user = userRepo.getUser()
                user?.let {
                    _success.emit(it.role)
                }
                _isLoading.emit(false)
            }
    }

    }

    private fun validate(email: String, pass: String) {
        if(email.isEmpty() || pass.isEmpty()) {
            throw Exception("Error!")
        }
    }
}