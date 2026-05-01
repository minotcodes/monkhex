import * as admin from "firebase-admin";
import * as functions from "firebase-functions";

admin.initializeApp();

const commitmentCollection = admin.firestore().collection("commitment");

export const applyCommitmentPenalty = functions.https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError("unauthenticated", "Authentication required.");
  }

  const userId = String(data.userId || "").trim();
  const missedToday = Boolean(data.missedToday);

  if (!userId || context.auth.uid !== userId) {
    throw new functions.https.HttpsError("permission-denied", "Invalid user context.");
  }

  const docRef = commitmentCollection.doc(userId);
  const snapshot = await docRef.get();
  if (!snapshot.exists) {
    throw new functions.https.HttpsError("failed-precondition", "Commitment plan missing.");
  }

  const plan = snapshot.data() as {
    planAmount: number;
    missedDays: number;
    consecutiveMisses: number;
    penalty: number;
    refundAmount: number;
  };

  const previousMissedDays = Number(plan.missedDays || 0);
  const previousConsecutive = Number(plan.consecutiveMisses || 0);
  const previousPenalty = Number(plan.penalty || 0);
  const planAmount = Number(plan.planAmount || 0);

  let penaltyAppliedToday = 0;
  let nextMissedDays = previousMissedDays;
  let nextConsecutive = previousConsecutive;

  if (missedToday) {
    const basePenalty = previousMissedDays < 2 ? 10 : 15;
    nextConsecutive = previousConsecutive + 1;
    const consecutivePenalty = nextConsecutive === 3 ? 20 : 0;
    penaltyAppliedToday = basePenalty + consecutivePenalty;
    nextMissedDays = previousMissedDays + 1;
  } else {
    nextConsecutive = 0;
  }

  const totalPenalty = previousPenalty + penaltyAppliedToday;
  const refundAmount = Math.max(0, planAmount - totalPenalty);

  await docRef.set(
    {
      missedDays: nextMissedDays,
      consecutiveMisses: nextConsecutive,
      penalty: totalPenalty,
      refundAmount,
      updatedAt: admin.firestore.FieldValue.serverTimestamp(),
    },
    { merge: true }
  );

  return {
    penaltyAppliedToday,
    totalPenalty,
    refundAmount,
    missedDays: nextMissedDays,
    consecutiveMisses: nextConsecutive,
  };
});
