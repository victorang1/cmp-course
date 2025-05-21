package org.example.cmpcourse.settings

import com.russhwolf.settings.Settings

object AppSettings {

    private val settings: Settings = Settings()

    fun putString(key: String, value: String) {
        settings.putString(key, value)
    }

    fun getString(key: String, default: String = ""): String {
        return settings.getString(key, default)
    }

    fun logout() {
        settings.remove(EMAIL)
    }

    const val EMAIL = "email"
}