package com.example.mob.ui.teacher.editQuiz

import androidx.lifecycle.viewModelScope
import com.example.mob.data.model.Question
import com.example.mob.data.repo.QuizRepo
import com.example.mob.ui.teacher.base.BaseAddEditQuizViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditQuizViewModel @Inject constructor(
    private val quizRepository: QuizRepo
) : BaseAddEditQuizViewModel()  {

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    fun getQuizById(quizId: String) {
        viewModelScope.launch {
            quiz.value = quizRepository.getQuizById(quizId)
        }
    }

    fun setQuestions(questions: List<Question>) {
        _questions.value = questions
    }

    override fun saveQuiz(
        title: String,
        publishDate: String
    ) {
        quiz.value?.let {
            val newQuiz = it.copy(
                title = title,
                publishDate = parsingDate(publishDate),
                questions = _questions.value
            )
            viewModelScope.launch {
                quizRepository.updateQuiz(newQuiz)
                finish.emit(Unit)
            }
        }
    }
}