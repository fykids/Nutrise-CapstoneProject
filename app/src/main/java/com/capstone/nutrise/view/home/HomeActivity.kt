package com.capstone.nutrise.view.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.capstone.nutrise.R
import com.capstone.nutrise.databinding.ActivityHomeBinding
import com.capstone.nutrise.view.camera.CameraActivity
import com.capstone.nutrise.view.onboarding.OnBoardingActivity
import com.capstone.nutrise.view.settings.SettingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        auth = Firebase.auth
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        /*
        Melakukan cek data apakah user sudah login, ketika sudah maka tidak perlu menampilkan
        Onboarding kembali ygy
         */
        // nah disini logikanya
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val hasShownToast = sharedPreferences.getBoolean("hasShownToast", false)
            if (!hasShownToast) {
                Toast.makeText(this, "Kamu Kembali!: ${currentUser.email}", Toast.LENGTH_SHORT).show()
                sharedPreferences.edit().putBoolean("hasShownToast", true).apply()
            }
        } else {
            val intent = Intent(this, OnBoardingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        val user = Firebase.auth.currentUser

        binding.nameUser.text = user?.displayName?.takeIf { it.isNotEmpty() } ?: user?.email.orEmpty()

//        binding.nameUser.text = user?.displayName?.takeIf { it.isNotEmpty() } ?: user?.email.toString()

        @Suppress("DEPRECATION")
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeMenu -> {
                    true
                }

                R.id.settingMenu -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }

                else -> false
            }
        }
        // Inisialisasi LinearLayout untuk membuka CameraActivity
        val linearLayout2 = findViewById<LinearLayout>(R.id.linearLayout2)
        linearLayout2.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
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
        val bottomNavigation: BottomNavigationView = binding.bottomNavigationView
        bottomNavigation.selectedItemId = R.id.homeMenu
    }
}