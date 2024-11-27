package com.capstone.nutrise.view.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        binding.button.setOnClickListener(this)
    }

    override fun onClick(v : View) {
        when (v.id) {
            R.id.button -> {
                startActivity(Intent(this@OnBoardingActivity, LoginActivity::class.java))
            }
        }
    }
}
