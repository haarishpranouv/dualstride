package com.example.dualstride.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dualstride.data.model.UserProfile
import com.example.dualstride.data.repository.GuardianRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val profile   : UserProfile = UserProfile(),
    val isLoading : Boolean     = true,
    val isSaving  : Boolean     = false,
    val saved     : Boolean     = false,
    val error     : String?     = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: GuardianRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    val currentEmail = repo.currentUserEmail()

    init { loadProfile() }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                val profile = repo.getProfile() ?: UserProfile()
                _uiState.update { it.copy(profile = profile, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun updateProfile(updated: UserProfile) { _uiState.update { it.copy(profile = updated) } }

    fun saveProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                repo.saveProfile(_uiState.value.profile)
                _uiState.update { it.copy(isSaving = false, saved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }

    fun logout()     = repo.logout()
    fun clearSaved() = _uiState.update { it.copy(saved = false) }
}