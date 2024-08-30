package com.example.mob.data.repo

import com.example.mob.core.service.AuthService
import com.example.mob.data.model.StudentResult
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StudentResultRepo @Inject constructor(private val authService: AuthService) {
    private fun getCollection(quizId: String, studentId: String): CollectionReference {
        val uid = authService.getUid() ?: throw Exception("User ID doesn't exist")
        return Firebase.firestore.collection("quizzes/$quizId/students/$studentId/results")
    }

    fun getAllResults(quizId: String, studentId: String) = callbackFlow<List<StudentResult>> {
        val listener = getCollection(quizId, studentId).addSnapshotListener { value, error ->
            if (error != null) {
                throw error
            }
            val results = mutableListOf<StudentResult>()
            value?.documents?.map { item ->
                item.data?.let { resultMap ->
                    val result = StudentResult.fromMap(resultMap)
                    results.add(result)
                }
            }
            trySend(results)
        }
        awaitClose {
            listener.remove()
        }
    }

    suspend fun hasResult(quizId: String, studentId: String): Boolean {
        val query = Firebase.firestore.collection("quizzes/$quizId/students/$studentId/results")
            .get()
            .await()

        return !query.isEmpty
    }

    suspend fun addResult(quizId: String, studentId: String, result: StudentResult): String? {
        val response = getCollection(quizId, studentId).add(result.toMap()).await()
        return response?.id
    }

    suspend fun deleteResult(quizId: String, studentId: String, resultId: String) {
        getCollection(quizId, studentId).document(resultId).delete().await()
    }

    suspend fun getResultById(quizId: String, studentId: String, resultId: String): StudentResult? {
        val res = getCollection(quizId, studentId).document(resultId).get().await()
        return res.data?.let { StudentResult.fromMap(it) }
    }
}