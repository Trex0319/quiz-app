package com.example.mob.core.service

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthService{
    private val auth =  FirebaseAuth.getInstance()

    suspend fun loginWithEmailAndPass(email: String, pass: String): FirebaseUser? {
        val res = auth.signInWithEmailAndPassword(email, pass).await()
        return res.user
    }

    suspend fun createUserWithEmailAndPass(email: String, pass: String): Boolean {
        val res = auth.createUserWithEmailAndPassword(email, pass).await()
        return res.user != null
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser?  {
        return auth.currentUser
    }

    fun getUid(): String? {
        return auth.currentUser?.uid
    }
}