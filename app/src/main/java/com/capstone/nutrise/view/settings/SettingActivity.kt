package com.capstone.nutrise.view.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.capstone.nutrise.R
import com.capstone.nutrise.databinding.ActivitySettingBinding
import com.capstone.nutrise.firebase.AuthViewModelFactory
import com.capstone.nutrise.firebase.repository.AuthRepository
import com.capstone.nutrise.view.auth.login.LoginActivity
import com.capstone.nutrise.view.home.HomeActivity
import com.capstone.nutrise.view.settings.account.AccountFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingBinding
    private lateinit var nightModeLayout : View
    private lateinit var nightModeIcon : ImageView
    private val settingViewModel : SettingViewModel by viewModels {
        AuthViewModelFactory(application, AuthRepository())
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nightModeLayout = binding.nightMode
        nightModeIcon = binding.imageNightMode

        // Mengupdate UI pertama kali saat activity dibuka
        lifecycleScope.launch {
            settingViewModel.isNightMode.collect { isNightMode ->
                updateNightModeUI(isNightMode)
            }
        }

        setupView()

        val auth = Firebase.auth
        val currentUser = auth.currentUser

        binding.nameUser.text = currentUser?.displayName?.takeIf { it.isNotEmpty() } ?: "User"

        binding.userSetting.setOnClickListener {
            val fragment = AccountFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        @Suppress("DEPRECATION")
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeMenu -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.settingMenu -> {
                    true
                }

                else -> false
            }
        }

        binding.logoutButton.setOnClickListener {
            Log.d(TAG, "userLogout: logoutSuccess")
            settingViewModel.logout()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        // Ketika mode malam diubah
        binding.nightMode.setOnClickListener {
            lifecycleScope.launch {
                settingViewModel.toggleNightMode()
                val updatedIsNightMode = settingViewModel.isNightMode.first()
                updateNightModeUI(updatedIsNightMode)
                setAppNightMode(updatedIsNightMode)
            }
        }
    }

    // Fungsi untuk memperbarui UI berdasarkan status mode malam
    private fun updateNightModeUI(isNightMode : Boolean) {
        if (isNightMode) {
            nightModeIcon.setImageResource(R.drawable.baseline_light_mode_24)
            binding.textMode.text = "Mode Siang"
            setAppNightMode(true)
        } else {
            nightModeIcon.setImageResource(R.drawable.baseline_mode_night_dark)
            binding.textMode.text = "Mode Malam"
            setAppNightMode(false)
        }
    }

    private fun setAppNightMode(isNightMode : Boolean) {
        val nightModeFlags = if (isNightMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightModeFlags)
    }

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
