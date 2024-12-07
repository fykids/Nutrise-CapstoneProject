package com.capstone.nutrise

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Memeriksa dan menerapkan mode malam berdasarkan preferensi
        val isNightMode = getNightModePreference()
        setAppNightMode(isNightMode)
    }

    // Fungsi untuk memeriksa mode malam dari SharedPreferences
    private fun getNightModePreference(): Boolean {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        return sharedPreferences.getBoolean("NIGHT_MODE", false) // Default ke mode siang
    }

    // Fungsi untuk mengubah mode malam aplikasi
    private fun setAppNightMode(isNightMode: Boolean) {
        val nightModeFlags = if (isNightMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightModeFlags)
    }
}
