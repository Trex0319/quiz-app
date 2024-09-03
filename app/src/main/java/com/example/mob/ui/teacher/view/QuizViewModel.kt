package com.example.mob.ui.teacher.view

import com.example.mob.data.model.Quiz
import com.example.mob.data.model.StudentQuizCompletion
import com.example.mob.data.model.StudentResult
import com.example.mob.data.repo.QuizRepo
import com.example.mob.data.repo.StudentQuizRepo
import com.example.mob.data.repo.StudentResultRepo
import com.example.mob.data.repo.UserRepo
import com.example.mob.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val quizRepo: QuizRepo,
    private val userRepo: UserRepo,
    private val completionRepo: StudentQuizRepo,
    private val resultRepo: StudentResultRepo
) : BaseViewModel() {
    suspend fun getQuizById(quizId: String): Quiz? {
        return quizRepo.getQuizById(quizId)
    }

    fun calculateScore(quiz: Quiz, selectedAnswers: Map<String, String>): Int {
        var totalScore = 0
        quiz.questions.forEach { question ->
            val selectedAnswer = selectedAnswers[question.questionId]
            if (selectedAnswer == question.correctAnswer) {
                totalScore += question.mark
            }
        }
        return totalScore
    }

    suspend fun getCurrentUserId(): String {
        return userRepo.getUid()
    }

    suspend fun saveResult(quizId: String, studentId: String, score: Int) {
        _isLoading.emit(true)
        try {
            if (resultRepo.hasResult(quizId, studentId)) return

            val user = userRepo.getUserDetails(studentId)
            val result = StudentResult(
                studentId = studentId,
                quizId = quizId,
                score = score,
                submittedAt = System.currentTimeMillis()
            )
            resultRepo.addResult(quizId, studentId, result)

            val totalScore = (completionRepo.getCompletionByStudentId(studentId)?.totalScore ?: 0) + score
            completionRepo.addCompletion(StudentQuizCompletion(studentId, user?.name.orEmpty(), totalScore))

            quizRepo.getQuizById(quizId)?.takeIf { !it.status }?.let {
                quizRepo.updateQuiz(it.copy(status = true))
            }
            finish.emit(Unit)
        } catch (e: Exception) {
            _error.emit(e.message ?: "Error saving")
        } finally {
            _isLoading.emit(false)
        }
    }

}