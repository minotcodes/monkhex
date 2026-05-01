# MonkHex Android (Kotlin + Compose + Firebase)

MonkHex is a premium discipline system that combines:
- Daily mission generation (single data-driven algorithm)
- Habit completion tracking and streaks
- XP + level progression
- Commitment penalty/refund logic
- Optional Gemini daily task suggestions with Firebase cache
- FCM reminders and alerts

## Tech Stack
- Kotlin + Jetpack Compose
- MVVM + Clean architecture boundaries
- Hilt DI
- Firebase Auth + Firestore + FCM + Functions
- Retrofit + OkHttp + Moshi

## Package Structure
- `core/` shared algorithm + theming + common utilities
- `domain/` entities, repository contracts, use cases
- `data/` Firestore/Gemini sources and repository implementations
- `feature/home` production dashboard UI and VM
- `feature/tasks`, `feature/commitment`, `feature/stats`, `feature/profile`
- `navigation/` app navigation shell + bottom bar
- `notifications/` FCM service

## Important System Rules Implemented
- All addiction inputs map into only 3 categories (`digital`, `mental`, `physical`)
- One unified task algorithm pipeline:
  1. Read addiction input
  2. Map to category
  3. Fetch tasks
  4. Score by difficulty fit + past usage suppression + randomness
  5. Return 5-6 tasks
- Commitment penalty model:
  - first 2 misses: INR 10
  - later misses: INR 15
  - 3 consecutive misses: +INR 20
  - refund: `planAmount - penalty`, floored at zero

## Firebase Collections
- `users`
- `tasks`
- `user_tasks`
- `commitment`
- `ai_suggestions`

Firestore rules and Cloud Function are included under `firebase/`.

## Setup
1. Install JDK 17+ and ensure `java` is available in PATH.
2. Add your Firebase `google-services.json` at `app/google-services.json`.
3. Add API keys in `local.properties`:
   - `GEMINI_API_KEY=...`
4. Deploy cloud function in `firebase/functions` for server-authoritative penalty updates.

## Credential Locations
- Android + Firebase app credentials:
  - `app/google-services.json` (copy from Firebase Console > Project Settings > Your apps > Android app)
  - Template: `app/google-services.json.example`
- Gemini API credential:
  - `local.properties` -> `GEMINI_API_KEY=YOUR_KEY`
  - Template: `local.properties.example`
- Firebase CLI deploy target (for Functions deployment):
  - `.firebaserc` with your Firebase project id
  - Template: `.firebaserc.example`

## Notes
- The app contains debug fake-AI fallback and real AI path with one-call/day cache logic.
- If Gemini key is missing, AI suggestions automatically fall back to local generated tasks.
- Unit and Compose UI test scaffolding are included.
