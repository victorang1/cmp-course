package org.example.cmpcourse.repository

import org.example.cmpcourse.settings.AppSettings

class AppRepository {

    fun getLoggedInEmail(): String {
        return AppSettings.getString(AppSettings.EMAIL, "")
    }
}