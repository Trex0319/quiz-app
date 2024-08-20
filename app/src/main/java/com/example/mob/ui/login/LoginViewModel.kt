package com.example.mob.ui.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.mob.core.service.AuthService
import com.example.mob.data.repo.UserRepo
import com.example.mob.ui.base.BaseViewModel
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor (
    private val authService: AuthService,
    private val userRepo: UserRepo,
) : BaseViewModel() {
    val success: MutableSharedFlow<Unit> = MutableSharedFlow()

    fun login(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                validate(email, pass)
                authService.loginWithEmailAndPass(email, pass)
            }?.let {
                success.emit(Unit)
            }
    }

    }

    private fun validate(email: String, pass: String) {
        if(email.isEmpty() || pass.isEmpty()) {
            throw Exception("Stupid!!!")
        }
    }

    private fun getGreetings() {
        viewModelScope.launch {
            errorHandler {
//                repo.getGreetings()
            }?.let {
                Log.d("debugging", it.toString())
            }
        }
    }
}