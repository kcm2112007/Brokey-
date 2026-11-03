# ====================================================================
# BROKEY Android Keyboard - ProGuard / R8 Optimization & Obfuscation Rules
# Target: Production Google Play Store Release (AAB / APK)
# ====================================================================

# --------------------------------------------------------------------
# 1. Android InputMethodService & Core IME Components
# Critical: Keep the IME service and custom views so the Android OS
# can bind to BrokeyInputMethodService and inflate layouts via reflection.
# --------------------------------------------------------------------
-keep public class com.brokey.keyboard.BrokeyInputMethodService extends android.inputmethodservice.InputMethodService {
    public <init>();
    *;
}

-keep public class com.brokey.keyboard.KeyboardView extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep public class com.brokey.MainActivity extends androidx.appcompat.app.AppCompatActivity {
    public <init>();
    *;
}

# --------------------------------------------------------------------
# 2. ViewBinding and Custom Views
# Ensure generated ViewBinding classes and constructors are retained
# --------------------------------------------------------------------
-keep class com.brokey.databinding.** { *; }
-keepclassmembers class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

# --------------------------------------------------------------------
# 3. Data Models, Languages & Transliteration Dictionaries
# Preserve Trie nodes, LanguageConfig, ClipItem, and theme data models
# --------------------------------------------------------------------
-keep class com.brokey.languages.** { *; }
-keep class com.brokey.transliteration.** { *; }
-keep class com.brokey.dictionaries.** { *; }
-keep class com.brokey.clipboard.ClipItem { *; }
-keep class com.brokey.themes.KeyboardTheme { *; }

# --------------------------------------------------------------------
# 4. Room Database & SQLite Persistence
# Preserve Room entity classes, DAOs, and database implementations
# --------------------------------------------------------------------
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Dao interface * { *; }
-keep @androidx.room.Entity class * { *; }

-dontwarn androidx.room.paging.**

# --------------------------------------------------------------------
# 5. AndroidX DataStore & Preferences
# --------------------------------------------------------------------
-keep class androidx.datastore.** { *; }
-keep class * extends androidx.datastore.preferences.core.Preferences { *; }

# --------------------------------------------------------------------
# 6. Kotlin Coroutines & Flow
# --------------------------------------------------------------------
-keep class kotlinx.coroutines.** { *; }
-keepclassmembers class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# --------------------------------------------------------------------
# 7. Zero Telemetry & Security Hardening
# Ensure no logging or reflection leaks sensitive input state
# --------------------------------------------------------------------
# Strip debugging and verbose logs from release builds
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
    public static int i(...);
}

# Preserve line numbers and file names for crash symbolication
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
