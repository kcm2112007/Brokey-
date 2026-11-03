# Google Play Console – Data Safety Questionnaire Guide

Use this reference when filling out the mandatory **Data Safety** section in Google Play Console for **BROKEY** (`com.brokey.keyboard`).

---

## 1. Data Collection and Security Overview

| Play Console Question | Answer | Notes / Developer Justification |
|---|---|---|
| **Does your app collect or share any of the required user data types?** | **No** | BROKEY processes all inputs on-device. No data is collected, logged, or shared. |
| **Is all user data collected by your app encrypted in transit?** | **N/A** | Not applicable because no user data is collected or transmitted off-device. |
| **Do you provide a way for users to request that their data be deleted?** | **N/A** | No account creation or server storage exists. Local transient clipboard history has a single-tap "Clear All" button. |

---

## 2. Detailed Data Category Breakdown

The following data categories are **NOT COLLECTED** and **NOT SHARED**:

| Category | Sub-types | Status in BROKEY |
|---|---|---|
| **Location** | Approximate location, Precise location | ❌ NOT COLLECTED |
| **Personal Info** | Name, Email, User IDs, Address, Phone, Race/Ethnicity, Political/Religious beliefs, Sexual orientation | ❌ NOT COLLECTED |
| **Financial Info** | Credit card numbers, Bank account, Purchase history, Credit score | ❌ NOT COLLECTED (Disabled in password/credit card fields) |
| **Health and Fitness** | Health info, Fitness info | ❌ NOT COLLECTED |
| **Messages** | Emails, SMS/MMS, Other in-app messages | ❌ NOT COLLECTED (Keystrokes passed to target app via OS InputConnection) |
| **Photos and Videos** | Photos, Videos | ❌ NOT COLLECTED |
| **Audio Files** | Voice recordings, Sound recordings | ❌ NOT COLLECTED (Audio handled by Android SpeechRecognizer OS service) |
| **Files and Docs** | Files, Documents | ❌ NOT COLLECTED |
| **Calendar** | Calendar events | ❌ NOT COLLECTED |
| **Contacts** | Contacts | ❌ NOT COLLECTED |
| **App Activity** | Page views, Clicks, In-app search, Installed apps | ❌ NOT COLLECTED |
| **Web Browsing** | Web browsing history | ❌ NOT COLLECTED |
| **App Info & Performance** | Crash logs, Diagnostics, Other performance data | ❌ NOT COLLECTED (Zero third-party crash analytics SDKs) |
| **Device or Other IDs** | Device ID, Advertising ID (AAID) | ❌ NOT COLLECTED |

---

## 3. Play Store Security Declaration Checklist

- [x] **No `INTERNET` permission in `AndroidManifest.xml`**
- [x] **No third-party advertising SDKs**
- [x] **No third-party analytics SDKs**
- [x] **No remote server telemetry**
- [x] **Passwords shielded:** Typing in `TYPE_TEXT_VARIATION_PASSWORD` fields bypasses clipboard and suggestion history
- [x] **ProGuard / R8 code obfuscation and debug log stripping enabled in `proguard-rules.pro`**
- [x] **Family Link / Designed for Families friendly**
