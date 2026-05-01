# Keep class names for Hilt and Firebase entry points
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class com.google.firebase.** { *; }

# Preserve Kotlin metadata
-keep class kotlin.Metadata { *; }
