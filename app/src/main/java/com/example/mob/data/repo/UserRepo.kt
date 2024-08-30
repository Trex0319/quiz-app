package com.example.mob.data.repo

import com.example.mob.core.service.AuthService
import com.example.mob.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class UserRepo @Inject constructor (
    private val authService: AuthService
) {
    suspend fun getUid(): String {
        return Firebase.auth.currentUser?.uid ?: throw Exception("User doesn't exist")
    }

    private fun getUserCallRef(): CollectionReference {
        return Firebase.firestore.collection("users")
    }

    suspend fun createUser(user: User) {
        getUserCallRef().document(getUid()).set(user).await()
    }

    suspend fun getUser(): User? {
        val res = getUserCallRef().document(getUid()).get().await()
        return res.data?.let { User.fromMap(it) }
    }

    suspend fun updateUser(user: User) {
        getUserCallRef().document(getUid()).set(user).await()
    }

    suspend fun getUserDetails(studentId: String): User? {
        return try {
            val documentSnapshot = getUserCallRef().document(studentId).get().await()

            if (documentSnapshot.exists()) {
                documentSnapshot.data?.let { User.fromMap(it) }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}