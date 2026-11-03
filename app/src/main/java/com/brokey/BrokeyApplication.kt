package com.brokey

import android.app.Application
import com.brokey.settings.KeyboardPreferences

/**
 * Main application context for BROKEY.
 * Initializes preferences and offline language dictionaries.
 */
class BrokeyApplication : Application() {

    lateinit var preferences: KeyboardPreferences
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        preferences = KeyboardPreferences(this)
    }

    companion object {
        lateinit var instance: BrokeyApplication
            private set
    }
}
