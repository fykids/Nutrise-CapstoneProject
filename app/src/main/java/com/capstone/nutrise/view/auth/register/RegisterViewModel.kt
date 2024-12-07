package com.capstone.nutrise.view.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrise.firebase.model.UserModelFirebase
import com.capstone.nutrise.firebase.repository.AuthRepository
import kotlinx.coroutines.launch
import com.capstone.nutrise.firebase.repository.Result

class RegisterViewModel(private val authRepository : AuthRepository) : ViewModel() {

    fun registeredUser(
        user : UserModelFirebase,
        onResult : (Result<UserModelFirebase>) -> Unit
    ) {
        viewModelScope.launch {
            onResult(Result.Loading)
            val result = authRepository.register(user)
            onResult(result)
        }
    }
}