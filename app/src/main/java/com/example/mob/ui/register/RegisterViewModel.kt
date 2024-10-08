package com.example.mob.ui.register

import androidx.lifecycle.viewModelScope
import com.example.mob.core.service.AuthService
import com.example.mob.core.utils.UserRole
import com.example.mob.core.utils.Validation
import com.example.mob.data.model.User
import com.example.mob.data.model.ValidationField
import com.example.mob.data.repo.UserRepo
import com.example.mob.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo
) : BaseViewModel() {
    fun register(name: String, email: String, password: String, confirmPassword: String, role: String) {
        val error = Validation.validate(
            ValidationField(name,"[a-zA-Z ]{2,20}", "Enter a valid name"),
            ValidationField(email,"[a-zA-Z0-9]+@[a-zA-Z0-9]+.[a-zA-Z0-9]+", "Enter a valid email"),
            ValidationField(password,"[a-zA-Z0-9#$%]{6,20}", "Enter a valid password"),
            ValidationField(role, "Teacher|Student", "Select a valid role")
        )

        if(error == null) {
            viewModelScope.launch(Dispatchers.IO) {
                val selectedRole = when (role) {
                    "Teacher" -> UserRole.Teacher
                    "Student" -> UserRole.Student
                    else -> UserRole.Student
                }
                errorHandler {
                    authService.createUserWithEmailAndPass(email, password)
                }?.let {
                    userRepo.createUser(
                        User(name, email, selectedRole)
                    )
                    _success.emit(selectedRole)
                }
            }
        } else {
            viewModelScope.launch {
                _error.emit(error)
            }
        }
    }
}