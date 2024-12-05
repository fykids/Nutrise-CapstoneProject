package com.capstone.nutrise.view.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.nutrise.databinding.ActivityHomeBinding
import com.capstone.nutrise.view.onboarding.OnBoardingActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        /*
        Melakukan cek data apakah user sudah login, ketika sudah maka tidak perlu menampilkan
        Onboarding kembali ygy
         */
        // nah disini logikanya
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Toast.makeText(this, "Kamu Kembali!: ${currentUser.email}", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, OnBoardingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        val user = Firebase.auth.currentUser
        user

        binding.nameUser.text = user?.email.toString()
        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        Firebase.auth.signOut()

        val intent = Intent(this, OnBoardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}