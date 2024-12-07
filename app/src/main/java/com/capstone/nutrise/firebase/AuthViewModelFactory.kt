package com.capstone.nutrise.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.nutrise.firebase.repository.AuthRepository
import com.capstone.nutrise.view.auth.login.LoginViewModel
import com.capstone.nutrise.view.auth.register.RegisterViewModel
import com.capstone.nutrise.view.settings.SettingViewModel
import com.capstone.nutrise.view.settings.account.AccountViewModel

class AuthViewModelFactory(
    private val application : android.app.Application,
    private val authRepository : AuthRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass : Class<T>) : T {
        @Suppress("UNCHECKED_CAST")
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            return AccountViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(application, authRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}