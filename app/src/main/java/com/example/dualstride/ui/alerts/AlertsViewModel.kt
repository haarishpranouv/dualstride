package com.guardianwear.app.ui.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guardianwear.app.data.model.AlertEvent
import com.guardianwear.app.data.repository.GuardianRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlertsUiState(
    val alerts    : List<AlertEvent> = emptyList(),
    val isLoading : Boolean          = true,
    val error     : String?          = null
)

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val repo: GuardianRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlertsUiState())
    val uiState: StateFlow<AlertsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repo.observeAlerts()
                .catch { e -> _uiState.update { it.copy(error = e.message, isLoading = false) } }
                .collect { list -> _uiState.update { it.copy(alerts = list, isLoading = false) } }
        }
    }

    fun resolveAlert(alertId: String) {
        viewModelScope.launch {
            try { repo.resolveAlert(alertId) }
            catch (e: Exception) { _uiState.update { it.copy(error = e.message) } }
        }
    }
}