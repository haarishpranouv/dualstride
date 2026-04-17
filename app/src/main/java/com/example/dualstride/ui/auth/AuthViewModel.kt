package com.example.dualstride.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dualstride.data.repository.GuardianRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading : Boolean = false,
    val error     : String? = null,
    val success   : Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: GuardianRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    val isLoggedIn = MutableStateFlow(repo.isLoggedIn())

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState(error = "Email and password are required")
            return
        }
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            try {
                repo.login(email.trim(), password)
                isLoggedIn.value = true
                _uiState.value = AuthUiState(success = true)
            } catch (e: Exception) {
                _uiState.value = AuthUiState(error = e.message ?: "Login failed")
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState(error = "All fields are required"); return
        }
        if (password != confirmPassword) {
            _uiState.value = AuthUiState(error = "Passwords do not match"); return
        }
        if (password.length < 6) {
            _uiState.value = AuthUiState(error = "Password must be at least 6 characters"); return
        }
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            try {
                repo.register(email.trim(), password)
                isLoggedIn.value = true
                _uiState.value = AuthUiState(success = true)
            } catch (e: Exception) {
                _uiState.value = AuthUiState(error = e.message ?: "Registration failed")
            }
        }
    }

    fun clearError() { _uiState.value = _uiState.value.copy(error = null) }
}