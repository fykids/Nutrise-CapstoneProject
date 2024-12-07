package com.capstone.nutrise.view.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrise.firebase.repository.AuthRepository
import com.capstone.nutrise.pref.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingViewModel(
    application : android.app.Application,
    private val authRepository : AuthRepository
) : ViewModel() {

    private val userPreferences = UserPreferences(application)

    private val _isNightMode = MutableStateFlow(false)
    val isNightMode : StateFlow<Boolean> = _isNightMode

    init {
        viewModelScope.launch{
            userPreferences.isNightMode.collect {
                _isNightMode.value = it
            }
        }
    }

    fun toggleNightMode() {
        viewModelScope.launch{
            val newMode = !_isNightMode.value
            userPreferences.settingNightMode(newMode)
            _isNightMode.value = newMode
        }
    }

    fun logout() {
        authRepository.logout()
    }
}