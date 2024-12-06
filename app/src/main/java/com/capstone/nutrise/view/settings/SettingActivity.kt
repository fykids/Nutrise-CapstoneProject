package com.capstone.nutrise.view.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.capstone.nutrise.R
import com.capstone.nutrise.databinding.ActivitySettingBinding
import com.capstone.nutrise.view.home.HomeActivity
import com.capstone.nutrise.view.onboarding.OnBoardingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class SettingActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingBinding
    private lateinit var nightModeLayout : View
    private lateinit var nightModeIcon : ImageView

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        setupView()
        // Initialize the views after binding
        nightModeLayout = binding.nightMode
        nightModeIcon = binding.imageNightMode

        @Suppress("DEPRECATION")
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeMenu -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.settingMenu -> {
                    true
                }

                else -> false
            }
        }

        // Ambil status mode malam dari SharedPreferences
        var isNightMode = sharedPreferences.getBoolean("NIGHT_MODE", false)
        updateNightModeUI(isNightMode)

        // Set listener pada RelativeLayout
        nightModeLayout.setOnClickListener {
            // Toggle mode malam
            isNightMode = !isNightMode
            editor.putBoolean("NIGHT_MODE", isNightMode)
            editor.apply()

            // Terapkan mode malam dan update UI
            AppCompatDelegate.setDefaultNightMode(
                if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
            updateNightModeUI(isNightMode)
        }

        binding.logoutButton.setOnClickListener {
            Log.d(TAG, "userLogout: logoutSuccess")
            logout()
        }
    }

    // Fungsi untuk memperbarui UI berdasarkan status mode malam
    private fun updateNightModeUI(isNightMode : Boolean) {
        if (isNightMode) {
            nightModeIcon.setImageResource(R.drawable.baseline_light_mode_24) // Ganti dengan ikon mode aktif
        } else {
            nightModeIcon.setImageResource(R.drawable.baseline_mode_night_dark) // Ganti dengan ikon default
        }
    }

    private fun logout() {
        Firebase.auth.signOut()

        val intent = Intent(this, OnBoardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }


    @Suppress("DEPRECATION")
    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigation : BottomNavigationView = binding.bottomNavigationView
        bottomNavigation.selectedItemId = R.id.settingMenu
    }

    companion object {
        const val TAG = "settingActivity"
    }
}