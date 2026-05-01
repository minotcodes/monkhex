package com.monkhex.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.monkhex.app.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun signInAnonymouslyIfNeeded(): String {
        val current = firebaseAuth.currentUser
        if (current != null) return current.uid
        return firebaseAuth.signInAnonymously().await().user?.uid
            ?: Firebase.auth.currentUser?.uid
            ?: error("Unable to authenticate user")
    }

    override fun currentUserId(): String? = firebaseAuth.currentUser?.uid
}

