package com.monkhex.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.monkhex.app.data.mapper.toDocument
import com.monkhex.app.data.mapper.toDomain
import com.monkhex.app.data.remote.model.UserProfileDocument
import com.monkhex.app.domain.model.AddictionCategory
import com.monkhex.app.domain.model.UserProfile
import com.monkhex.app.domain.repository.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    private val usersCollection = firestore.collection("users")

    override fun observeUserProfile(userId: String): Flow<UserProfile?> = callbackFlow {
        val registration = usersCollection.document(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(null)
                return@addSnapshotListener
            }
            val document = snapshot?.toObject(UserProfileDocument::class.java)
            trySend(document?.toDomain())
        }
        awaitClose { registration.remove() }
    }

    override suspend fun getUserProfile(userId: String): UserProfile? {
        val snapshot = usersCollection.document(userId).get().await()
        val existing = snapshot.toObject(UserProfileDocument::class.java)?.toDomain()
        if (existing != null) return existing

        val seeded = UserProfile(
            userId = userId,
            email = "",
            mode = "adaptive_moderate",
            addictionType = "Social media addiction",
            category = AddictionCategory.DIGITAL,
            level = 18,
            xp = 2450,
            streak = 12,
            disciplineScore = 78
        )
        upsertUserProfile(seeded)
        return seeded
    }

    override suspend fun upsertUserProfile(profile: UserProfile) {
        usersCollection.document(profile.userId).set(profile.toDocument()).await()
    }
}

