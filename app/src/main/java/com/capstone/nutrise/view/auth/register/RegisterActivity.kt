package com.capstone.nutrise.view.auth.register

import android.animation.Animator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.nutrise.databinding.ActivityRegisterBinding
import com.capstone.nutrise.firebase.AuthViewModelFactory
import com.capstone.nutrise.firebase.model.UserModelFirebase
import com.capstone.nutrise.firebase.repository.AuthRepository
import com.capstone.nutrise.firebase.repository.Result
import com.capstone.nutrise.view.auth.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding

    private val registerViewModel : RegisterViewModel by viewModels {
        AuthViewModelFactory(application, AuthRepository())
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        binding.buttonRegister.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Log.d(TAG, "Attempting login for email: $email")
                registerUser(email, password)
            } else {
                Log.w(TAG, "Email or Password is Empty")
            }
        }

        binding.buttonLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun registerUser(email : String, password : String) {
        val user = UserModelFirebase(email = email, password = password)
        registerViewModel.registeredUser(user) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    showSuccess(true)
                    binding.successLottie.addAnimatorListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {

                        }

                        override fun onAnimationEnd(animation: Animator) {
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }

                        override fun onAnimationCancel(animation: Animator) {}

                        override fun onAnimationRepeat(animation: Animator) {}
                    })
                }

                is Result.Error -> {
                    showLoading(false)
                    showError(true)
                    binding.errorLottie.addAnimatorListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {}

                        override fun onAnimationEnd(animation: Animator) {
                            binding.errorLottie.visibility = View.GONE
                            binding.buttonLogin.isEnabled = true
                            binding.buttonRegister.isEnabled = true
                        }

                        override fun onAnimationCancel(animation: Animator) {}

                        override fun onAnimationRepeat(animation: Animator) {}
                    })
                    binding.emailEditText.text?.clear()
                    binding.passwordEditText.text?.clear()
                }
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
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

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding.buttonLogin.isEnabled = false
            binding.loadingLottie.visibility = View.VISIBLE
            binding.loadingLottie.playAnimation()
        } else {
            binding.loadingLottie.visibility = View.GONE
            binding.buttonLogin.isEnabled = true
            binding.loadingLottie.pauseAnimation()
        }
    }

    private fun showError(isError : Boolean) {
        if (isError) {
            binding.buttonLogin.isEnabled = false
            binding.errorLottie.visibility = View.VISIBLE
            binding.errorLottie.playAnimation()
        } else {
            binding.errorLottie.visibility = View.GONE
            binding.buttonLogin.isEnabled = true
            binding.errorLottie.pauseAnimation()
        }
    }

    private fun showSuccess(isSuccess : Boolean) {
        if (isSuccess) {
            binding.buttonRegister.isEnabled = false
            binding.successLottie.playAnimation()
            binding.successLottie.visibility = View.VISIBLE
        } else {
            binding.successLottie.visibility = View.GONE
            binding.buttonRegister.isEnabled = true
            binding.successLottie.pauseAnimation()
        }
    }

    companion object {
        private const val TAG = "registerActivity"
    }
}