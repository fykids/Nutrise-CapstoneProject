package com.capstone.nutrise.view.settings.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrise.firebase.model.UserUpdateModelFirebase
import com.capstone.nutrise.firebase.repository.AuthRepository
import com.capstone.nutrise.firebase.repository.Result
import kotlinx.coroutines.launch

class AccountViewModel(private val authRepository : AuthRepository) : ViewModel() {
    fun updateUser(
        user : UserUpdateModelFirebase,
        onResult : (Result<UserUpdateModelFirebase>) -> Unit
    ) {
        viewModelScope.launch{
            val result = authRepository.updateUser(user)
            onResult(result)
        }
    }
}