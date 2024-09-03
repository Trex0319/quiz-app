package com.example.mob.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mob.data.model.StudentQuizCompletion
import com.example.mob.data.repo.QuizRepo
import com.example.mob.data.repo.StudentQuizRepo
import com.example.mob.data.repo.StudentResultRepo
import com.example.mob.data.repo.UserRepo
import com.example.mob.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val quizRepo: QuizRepo,
    private val userRepo: UserRepo,
    private val studentQuizRepo: StudentQuizRepo,
    private val resultRepo: StudentResultRepo
) : BaseViewModel() {

    private val _completions = MutableLiveData<List<StudentQuizCompletion>>()
    val completions: LiveData<List<StudentQuizCompletion>> get() = _completions

    fun loadCompletions() {
        viewModelScope.launch {
            studentQuizRepo.getAllCompletions().collect {
                if (it.isNotEmpty()) {
                    _completions.postValue(it.sortedByDescending { it.totalScore })
                }
            }
        }
    }

    suspend fun getCurrentUserId(): String {
        return userRepo.getUid()
    }


    suspend fun checkResult(quizId: String, studentId: String): Boolean {
        return resultRepo.hasResult(quizId, studentId)
    }

    suspend fun verifyQuizAccess(accessId: String): String? {
        _isLoading.emit(true)
        return try {
            quizRepo.getQuizIdByAccessId(accessId)
        } catch (e: Exception) {
            _error.emit(e.message ?: "verify error")
            null
        } finally {
            _isLoading.emit(false)
        }
    }
}