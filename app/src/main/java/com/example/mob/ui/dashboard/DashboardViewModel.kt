package com.example.mob.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mob.data.model.Quiz
import com.example.mob.data.repo.QuizRepo
import com.example.mob.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val quizRepository: QuizRepo
) : BaseViewModel() {

    private val _quiz = MutableLiveData<List<Quiz>>()
    val quiz: LiveData<List<Quiz>> = _quiz

    init {
        fetchQuizzes()
    }

    private fun fetchQuizzes() {
        viewModelScope.launch {
            quizRepository.getAllQuizzes().collect { quizList ->
                _quiz.postValue(quizList)
            }
        }
    }

    fun deleteQuiz(quizId: String) {
        viewModelScope.launch{
            errorHandler { quizRepository.deleteQuiz(quizId) }
        }
    }
}