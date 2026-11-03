# BROKEY Privacy Policy

**Effective Date:** October 31, 2026  
**Last Updated:** October 31, 2026  
**Application:** BROKEY – Indian Multilingual Android Keyboard (`com.brokey.keyboard`)  
**Developer Contact:** privacy@brokey.org / security@brokey.org  

---

## 1. Our Core Privacy Philosophy

At BROKEY, we believe that an Input Method Editor (IME) is the most intimate piece of software on any personal device. Every message you write to loved ones, every search query, every email, and every private thought passes through your keyboard.

**Our Guarantee:** BROKEY is built on an unconditional **Zero-Telemetry, Offline-First Architecture**. We do not collect, monitor, store, or transmit your keystrokes, personal communications, or private information to any external server.

---

## 2. Absence of Internet Permissions

The ultimate proof of privacy is architectural, not merely contractual:

- **BROKEY DOES NOT REQUEST OR POSSESS THE `android.permission.INTERNET` PERMISSION.**
- Because our application does not declare or request network access in `AndroidManifest.xml`, the Android operating system sandbox strictly prevents BROKEY from transmitting any data over Wi-Fi, cellular, or Bluetooth networks.
- Even if a malicious actor attempted to inject code into BROKEY, the operating system kernel blocks all outbound network socket connections.

---

## 3. Data Collection and Processing

| Data Type | Processed? | Stored Permanently? | Shared Externally? | Explanation |
|---|---|---|---|---|
| **Keystrokes & Typing** | On-Device Only | ❌ Never | ❌ Never | Keystrokes are converted to characters and immediately dispatched to the active target text field via the official Android `InputConnection`. They are never logged or persisted. |
| **Passwords & Credentials** | ❌ Never | ❌ Never | ❌ Never | When the active Android `EditorInfo` field specifies `TYPE_TEXT_VARIATION_PASSWORD` or `TYPE_NUMBER_VARIATION_PASSWORD`, BROKEY immediately disables suggestions, dictionaries, and clipboard history. |
| **Phonetic Transliteration** | On-Device Only | ❌ Never | ❌ Never | Transliteration algorithms and dictionaries execute entirely within the local memory space of the user's device. |
| **Autocorrect & Dictionaries** | On-Device Only | Local User Dictionary Only | ❌ Never | User-added custom dictionary words remain in an encrypted on-device SQLite database and never leave the device. |
| **Clipboard History** | Optional (In-Memory) | Transient Only | ❌ Never | BROKEY provides an optional in-memory clipboard manager. Unpinned clips are automatically wiped after 15 minutes. Users can instantly purge all clipboard history with a single tap. Clipboard is inaccessible over network. |
| **Voice Dictation** | Handled by OS | ❌ Never by Brokey | ❌ Never by Brokey | Voice typing triggers the device's default system `SpeechRecognizer` (e.g. Android on-device speech recognition). BROKEY does not record, buffer, or transmit audio recordings. |
| **Analytics & Telemetry** | ❌ None | ❌ None | ❌ None | BROKEY contains zero third-party tracking SDKs (no Firebase Analytics, no Google Analytics, no Facebook SDK, no AppsFlyer, no Mixpanel). |
| **Advertising Identifiers** | ❌ None | ❌ None | ❌ None | BROKEY contains zero ad networks and does not access the Google Advertising ID (AAID). |

---

## 4. Permissions Used by BROKEY

BROKEY requests only the bare minimum system permissions required to operate as an Android Input Method:

1. `android.permission.VIBRATE`:
   - **Purpose:** Used strictly to trigger haptic feedback vibrations when the user presses keyboard keys.
   - **User Control:** Can be toggled on/off in Keyboard Settings at any time.

2. `android.permission.RECORD_AUDIO` (Optional runtime request):
   - **Purpose:** Triggered only if the user explicitly taps the microphone icon for voice dictation.
   - **User Control:** The user may deny this permission, in which case text typing remains 100% operational.

---

## 5. Security & Isolation

- **Open Source Transparency:** The entire source code of BROKEY is inspectable and verifiable by independent security auditors.
- **ProGuard / R8 Obfuscation & Log Stripping:** In production builds, all diagnostic log calls (`Log.d`, `Log.v`, `Log.i`) are stripped at compile time to prevent any sensitive strings from appearing in system logcat.
- **No Third-Party Cloud Services:** We do not maintain or connect to any cloud databases or backends.

---

## 6. Children's Privacy (COPPA & GDPR-K Compliance)

BROKEY does not collect personal data from anyone, including children under the age of 13 (or under 16 in the European Union). The app contains no advertisements, no tracking, and no behavioral profiling, making it fully compliant with COPPA, GDPR, and Google Play Families Policy.

---

## 7. Changes to This Privacy Policy

Because BROKEY has no network access and cannot change its data practices dynamically, any updates to this policy will be clearly documented with each new version released via the Google Play Store.

---

## 8. Contact & Legal Entity

If you have questions, feedback, or would like to inspect our code verification audits, please contact:

**BROKEY Engineering Team**  
Email: `privacy@brokey.org`  
GitHub Repository: `https://github.com/brokey-keyboard/brokey-android`  
Location: Bengaluru, Karnataka, India
