package com.capstone.nutrise.view.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrise.firebase.model.UserModelFirebase
import com.capstone.nutrise.firebase.repository.AuthRepository
import com.capstone.nutrise.firebase.repository.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository : AuthRepository) : ViewModel() {

    fun login(email : String, password : String, onResult : (Result<UserModelFirebase>) -> Unit) {
        viewModelScope.launch {
            onResult(Result.Loading)
            val result = authRepository.login(email, password)
            onResult(result)
        }
    }
}