# BROKEY: Indian Multilingual Android Keyboard
> **Tagline:** One Keyboard. Every Indian Language.
> **Architecture:** Native Android InputMethodService (IME) in Kotlin

BROKEY is a native Android Input Method Editor (IME) engineered specifically for India's multilingual reality. It brings native script input and intelligent phonetic transliteration to all 22 constitutionally scheduled languages of India plus English.

---

## 1. System Requirements & Architecture
- **Language:** Kotlin 1.9+ / Java 17
- **Target Android Version:** Android 8.0 (API Level 26) through Android 15 (API Level 35)
- **Architecture:** Android `InputMethodService`, `InputConnection`, Jetpack DataStore, Room persistence
- **Zero-Telemetry Offline Privacy:** No `android.permission.INTERNET` requested. Keystrokes never leave your phone.

---

## 2. Supported Languages & Implementation Status

| # | Language | Native Script | Typing Method | Status |
|---|---|---|---|---|
| 0 | **English** | Latin (QWERTY) | Native & Numbers/Symbols | **Production** |
| 1 | **Hindi** (हिंदी) | Devanagari | Inscript Native + Phonetic Transliteration | **Production** |
| 2 | **Bengali** (বাংলা) | Eastern Nagari | Inscript Native + Phonetic Transliteration | **Production** |
| 3 | **Marathi** (मराठी) | Devanagari | Native Layout + Phonetic Transliteration | **Beta** |
| 4 | **Gujarati** (ગુજરાતી) | Gujarati | Native Layout + Phonetic Transliteration | **Beta** |
| 5 | **Punjabi** (ਪੰਜਾਬੀ) | Gurmukhi | Native Layout + Phonetic Transliteration | **Beta** |
| 6 | **Tamil** (தமிழ்) | Tamil | Native Layout + Phonetic Transliteration | **Beta** |
| 7 | **Telugu** (తెలుగు) | Telugu | Native Layout + Phonetic Transliteration | **Beta** |
| 8 | **Kannada** (ಕನ್ನಡ) | Kannada | Native Layout + Phonetic Transliteration | **Beta** |
| 9 | **Malayalam** (മലയാളം) | Malayalam | Native Layout + Phonetic Transliteration | **Beta** |
| 10 | **Odia** (ଓଡ଼ିଆ) | Odia | Native Layout + Phonetic Transliteration | **Beta** |
| 11 | **Urdu** (اردو) | Perso-Arabic (RTL) | Native Layout + Phonetic Transliteration | **Beta** |
| 12 | **Assamese** (অসমীয়া) | Eastern Nagari | Native Layout + Transliteration | **Alpha** |
| 13 | **Sanskrit** (संस्कृतम्) | Devanagari | Native Layout + Transliteration | **Alpha** |
| 14 | **Nepali** (नेपाली) | Devanagari | Native Layout + Transliteration | **Alpha** |
| 15 | **Konkani** (कोंकणी) | Devanagari | Native Layout + Transliteration | **Alpha** |
| 16 | **Kashmiri** (कॉशुर) | Perso-Arabic | Native Layout + Transliteration | **Alpha** |
| 17 | **Sindhi** (سنڌي) | Perso-Arabic / Devanagari | Native Layout + Transliteration | **Alpha** |
| 18 | **Manipuri** (Meitei) | Meetei Mayek | Native Layout + Transliteration | **Alpha** |
| 19 | **Bodo** (बड़ो) | Devanagari | Native Layout + Transliteration | **Alpha** |
| 20 | **Dogri** (डोगरी) | Devanagari | Native Layout + Transliteration | **Alpha** |
| 21 | **Maithili** (मैथिली) | Devanagari | Native Layout + Transliteration | **Alpha** |
| 22 | **Santali** (ᱥᱟᱱᱛᱟᱲᱤ) | Ol Chiki | Native Layout + Transliteration | **Alpha** |

---

## 3. Build & APK Generation Instructions

### Prerequisites
1. Install [Android Studio Hedgehog / Iguana / Jellyfish](https://developer.android.com/studio) or newer.
2. JDK 17 or higher.
3. Android SDK Platform 34 installed via SDK Manager.

### Step 1: Open or Clone Project
Unzip or clone the `android/` project folder into your workspace:
```bash
cd android
```

### Step 2: Build Debug APK via Command Line
Run the Gradle wrapper:
```bash
# On Linux / macOS:
./gradlew assembleDebug

# On Windows:
gradlew.bat assembleDebug
```

The compiled APK will be generated at:
```
android/app/build/outputs/apk/debug/app-debug.apk
```

### Step 3: Install on Device or Emulator via ADB
Ensure USB debugging is enabled on your Android smartphone, or boot an Android Emulator:
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## 4. Keyboard Activation Guide

1. **Open BROKEY App** on your Android device.
2. Tap **"Step 1: Enable BROKEY in Settings"**
   - This opens Android System Settings (`Settings > System > Languages & Input > On-screen keyboard`).
   - Toggle **BROKEY** ON. (Android will display its standard IME warning: "This input method may be able to collect all text...". Note that BROKEY has no network permission and runs entirely offline).
3. Tap **"Step 2: Choose BROKEY Input Method"**
   - In the system picker dialog, select **BROKEY**.
4. Test typing in the sandbox or open **WhatsApp, Telegram, Chrome, Instagram, or SMS** to type system-wide!

---

## 5. Testing Procedures
1. **System-wide IME verification:** Open WhatsApp / Chrome. Tap any text field. Verify BROKEY raises smoothly.
2. **Dual typing method verification:**
   - **Phonetic Transliteration:** Type `namaste` -> candidates show `नमस्ते`, `namaste`. Space bar commits `नमस्ते`.
   - **Native Script:** Tap language globe key -> select Hindi / Bengali. Tap native consonants & matras directly.
3. **Unicode conjunct testing:** Verify `क` + `्` + `ष` forms conjunct `क्ष` without box glyphs or corruptions.
4. **Haptic & Sound tests:** Toggle vibration in Settings -> feel distinct tactile pulse on keypress.
