package com.capstone.nutrise.view.settings.account

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.capstone.nutrise.databinding.FragmentAccountBinding
import com.capstone.nutrise.firebase.AuthViewModelFactory
import com.capstone.nutrise.firebase.model.UserUpdateModelFirebase
import com.capstone.nutrise.firebase.repository.AuthRepository
import com.capstone.nutrise.firebase.repository.Result
import com.capstone.nutrise.R
import com.capstone.nutrise.view.settings.SettingActivity

class AccountFragment : Fragment() {

    private lateinit var _binding : FragmentAccountBinding
    private val binding get() = _binding

    private val accountViewModel : AccountViewModel by activityViewModels {
        AuthViewModelFactory((requireActivity().application), AuthRepository())
    }

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        val toolbar = Toolbar(requireContext())

        toolbar.setBackgroundColor(R.style.ColorContainer)

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setTitle("Akun")

        toolbar.setNavigationOnClickListener{
            @Suppress("DEPRECATION")
            requireActivity().onBackPressed()
        }

        val layout = binding.root as ViewGroup
        layout.addView(toolbar, 0)

        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = binding.nameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val repassword = binding.rePasswordEditText.text.toString()

        if (name.isNotEmpty() && password.isNotEmpty() && repassword.isNotEmpty()) {
            if (password == repassword) {
                updateUser(name, password)
            } else {
                Toast.makeText(requireContext(), "Error Password is Wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun updateUser(name : String, password : String) {
        val user = UserUpdateModelFirebase(name, password)

        accountViewModel.updateUser(user) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    showSuccess(true)
                    binding.successLottie.addAnimatorListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation : Animator) {

                        }

                        override fun onAnimationEnd(animation : Animator) {
                            val intent = Intent(requireContext(), SettingActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }

                        override fun onAnimationCancel(animation : Animator) {}

                        override fun onAnimationRepeat(animation : Animator) {}
                    })
                }

                is Result.Error -> {
                    showLoading(false)
                    showError(true)

                    binding.errorLottie.addAnimatorListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation : Animator) {}

                        override fun onAnimationEnd(animation : Animator) {
                            binding.errorLottie.visibility = View.GONE
                            binding.buttonUpdateUser.isEnabled = true
                        }

                        override fun onAnimationCancel(animation : Animator) {}

                        override fun onAnimationRepeat(animation : Animator) {}
                    })
                    binding.nameEditText.text?.clear()
                    binding.passwordEditText.text?.clear()
                }
            }
        }
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding.buttonUpdateUser.isEnabled = false
            binding.loadingLottie.visibility = View.VISIBLE
            binding.loadingLottie.playAnimation()
        } else {
            binding.loadingLottie.visibility = View.GONE
            binding.loadingLottie.pauseAnimation()
            binding.buttonUpdateUser.isEnabled = true
        }
    }

    private fun showError(isError : Boolean) {
        if (isError) {
            binding.buttonUpdateUser.isEnabled = false
            binding.errorLottie.visibility = View.VISIBLE
            binding.errorLottie.playAnimation()
        } else {
            binding.errorLottie.visibility = View.GONE
            binding.errorLottie.pauseAnimation()
            binding.buttonUpdateUser.isEnabled = true
        }
    }

    private fun showSuccess(isSuccess : Boolean) {
        if (isSuccess) {
            binding.buttonUpdateUser.isEnabled = false
            binding.successLottie.visibility = View.VISIBLE
            binding.successLottie.playAnimation()
        } else {
            binding.successLottie.visibility = View.GONE
            binding.successLottie.pauseAnimation()
            binding.buttonUpdateUser.isEnabled = true
        }
    }

    @Suppress("SENSELESS_COMPARISON")
    override fun onDestroy() {
        super.onDestroy()
        _binding == null
    }
}