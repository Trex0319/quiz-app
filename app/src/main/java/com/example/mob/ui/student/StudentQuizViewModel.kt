package com.example.mob.ui.student

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
class StudentQuizViewModel @Inject constructor(
    private val quizRepo: QuizRepo,
    private val userRepo: UserRepo,
    private val studentQuizRepo: StudentQuizRepo,
    private val studentResultRepo: StudentResultRepo
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
            if (!studentResultRepo.hasResult(quizId, studentId)) {
                val user = userRepo.getUserDetails(studentId)
                val name = user?.name ?: ""
//                val profilePicture = user?.profilePicture ?: ""

                val result = StudentResult(
                    studentId = studentId,
                    quizId = quizId,
                    score = score,
                    submittedAt = System.currentTimeMillis()
                )
                studentResultRepo.addResult(quizId, studentId, result)

                val completion = StudentQuizCompletion(
                    studentId = studentId,
                    name = name,
//                    profilePicture = profilePicture,
                    totalScore = score
                )
                studentQuizRepo.addCompletion(completion)
                finish.emit(Unit)
            }
        } catch (e: Exception) {
            _error.emit(e.message ?: "Error saving result")
        } finally {
            _isLoading.emit(false)
        }
    }
}