package com.capstone.nutrise.view.onboarding

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.capstone.nutrise.R
import com.capstone.nutrise.databinding.ActivityOnBoardingBinding
import com.capstone.nutrise.view.auth.login.LoginActivity
import com.capstone.nutrise.view.onboarding.adapter.ItemAdapter
import com.capstone.nutrise.view.onboarding.model.OnboardingItem

class OnBoardingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewPager : ViewPager2
    private lateinit var adapter : ItemAdapter
    private lateinit var binding : ActivityOnBoardingBinding
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable : Runnable

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        // Data onboarding yang diambil dari drawable dan strings.xml
        val onboardingItems = listOf(
            OnboardingItem(
                R.drawable.onboarding1,
                getString(R.string.title_onboarding1),
                getString(R.string.description_onboarding1)
            ),
            OnboardingItem(
                R.drawable.onboarding2,
                getString(R.string.title_onboarding2),
                getString(R.string.description_onboarding2)
            ),
            OnboardingItem(
                R.drawable.onboarding3,
                getString(R.string.title_onboarding3),
                getString(R.string.description_onboarding3)
            )
        )

        // Menghubungkan adapter ke ViewPager2
        adapter = ItemAdapter(onboardingItems)
        viewPager = binding.vpImage
        viewPager.adapter = adapter

//        SetupAutoSlide
        setupAutoSlide()

        binding.button.setOnClickListener(this)
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAutoSlide() {
        runnable = Runnable {
            val currentItem = viewPager.currentItem
            val nextItem = if (currentItem == adapter.itemCount - 1) 0 else currentItem + 1
            viewPager.setCurrentItem(nextItem, true)
            handler.postDelayed(runnable, 5000) //jeda 5 detik
        }

        handler.postDelayed(runnable, 5000)
    }

    override fun onClick(v : View) {
        when (v.id) {
            R.id.button -> {
                startActivity(Intent(this@OnBoardingActivity, LoginActivity::class.java))
            }
        }
    }
}
