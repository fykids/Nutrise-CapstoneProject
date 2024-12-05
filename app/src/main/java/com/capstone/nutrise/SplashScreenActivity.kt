package com.capstone.nutrise

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.capstone.nutrise.databinding.ActivitySplashScreenBinding
import com.capstone.nutrise.view.auth.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // animasi
        playAnimation()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 3000)
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, "alpha", 0f, 1f).apply {
            duration = 2000 // Durasi animasi 2 detik
            start()
        }

        ObjectAnimator.ofFloat(binding.imageView, "translationY", -100f, 0f).apply {
            duration = 2000 // Animasi bergerak dari atas ke posisi semula
            start()
        }
    }
}