package com.example.mob.ui.teacher.quiz

import androidx.lifecycle.viewModelScope
import com.example.mob.data.model.Question
import com.example.mob.data.model.Quiz
import com.example.mob.data.repo.QuizRepo
import com.example.mob.ui.teacher.base.BaseAddEditQuizViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddQuizViewModel @Inject constructor(
    private val quizRepository: QuizRepo
) : BaseAddEditQuizViewModel() {

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    override fun saveQuiz(title: String, publishDate: String, expiryDate: String) {
        viewModelScope.launch {
            try {
                val quiz = Quiz(
                    quizId = generateQuizId(),
                    title = title,
                    questions = _questions.value,
                    publishDate = parsingDate(publishDate),
                    expiryDate = parsingDate(expiryDate),
                    createdBy = "",
                )
                quizRepository.addQuiz(quiz)
                finish.emit(Unit)
            } catch (e: Exception) {
                _error.emit(e.message ?: "Error")
            }
        }
    }

    fun setQuestions(questions: List<Question>) {
        _questions.value = questions
    }

    fun getQuestions(): List<Question> {
        return _questions.value
    }

    private fun generateQuizId(): String {
        return quizRepository.getNewQuizId()
    }
}