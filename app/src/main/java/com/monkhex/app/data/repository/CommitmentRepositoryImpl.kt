package com.monkhex.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.monkhex.app.core.algorithm.PenaltyCalculator
import com.monkhex.app.data.mapper.toDocument
import com.monkhex.app.data.mapper.toDomain
import com.monkhex.app.data.remote.model.CommitmentDocument
import com.monkhex.app.domain.model.CommitmentPlan
import com.monkhex.app.domain.model.CommitmentPlanType
import com.monkhex.app.domain.model.CommitmentStatus
import com.monkhex.app.domain.model.PenaltyLedger
import com.monkhex.app.domain.repository.CommitmentRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommitmentRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseFunctions: FirebaseFunctions
) : CommitmentRepository {

    private val commitmentCollection = firestore.collection("commitment")

    override fun observeCommitmentPlan(userId: String): Flow<CommitmentPlan?> = callbackFlow {
        val registration = commitmentCollection.document(userId)
            .addSnapshotListener { snapshot, _ ->
                val plan = snapshot?.toObject(CommitmentDocument::class.java)?.toDomain()
                trySend(plan)
            }
        awaitClose { registration.remove() }
    }

    override suspend fun getCommitmentPlan(userId: String): CommitmentPlan? {
        val existing = commitmentCollection.document(userId).get().await()
            .toObject(CommitmentDocument::class.java)
            ?.toDomain()
        if (existing != null) return existing

        val seeded = CommitmentPlan(
            userId = userId,
            planType = CommitmentPlanType.DAYS_14,
            planAmount = 299,
            startDate = LocalDate.now().toString(),
            missedDays = 0,
            consecutiveMisses = 0,
            penalty = 0,
            refundAmount = 299,
            status = CommitmentStatus.ACTIVE
        )
        runCatching { upsertCommitmentPlan(seeded) }
        return seeded
    }

    override suspend fun upsertCommitmentPlan(plan: CommitmentPlan) {
        commitmentCollection.document(plan.userId).set(plan.toDocument()).await()
    }

    override suspend fun computeAndApplyPenalty(userId: String, missedToday: Boolean): PenaltyLedger? {
        val functionResult = runCatching {
            firebaseFunctions
                .getHttpsCallable("applyCommitmentPenalty")
                .call(mapOf("userId" to userId, "missedToday" to missedToday))
                .await()
                .data as? Map<*, *>
        }.getOrNull()

        if (functionResult != null) {
            return PenaltyLedger(
                userId = userId,
                penaltyAppliedToday = (functionResult["penaltyAppliedToday"] as? Number)?.toInt() ?: 0,
                totalPenalty = (functionResult["totalPenalty"] as? Number)?.toInt() ?: 0,
                refundAmount = (functionResult["refundAmount"] as? Number)?.toInt() ?: 0,
                missedDays = (functionResult["missedDays"] as? Number)?.toInt() ?: 0,
                consecutiveMisses = (functionResult["consecutiveMisses"] as? Number)?.toInt() ?: 0
            )
        }

        val localPlan = getCommitmentPlan(userId) ?: return null
        val (updatedPlan, ledger) = PenaltyCalculator.applyDailyResult(localPlan, missedToday)
        runCatching { upsertCommitmentPlan(updatedPlan) }
        return ledger
    }
}
