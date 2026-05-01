package com.monkhex.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.monkhex.app.domain.repository.NotificationRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseMessaging: FirebaseMessaging
) : NotificationRepository {

    override suspend fun registerToken(userId: String, token: String) {
        firestore.collection("users")
            .document(userId)
            .set(mapOf("fcmToken" to token), SetOptions.merge())
            .await()
    }

    override suspend fun subscribeToCoreTopics() {
        firebaseMessaging.subscribeToTopic("daily_reminder").await()
        firebaseMessaging.subscribeToTopic("streak_alerts").await()
        firebaseMessaging.subscribeToTopic("penalty_warning").await()
    }
}
