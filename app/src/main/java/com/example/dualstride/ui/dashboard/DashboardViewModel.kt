package com.example.dualstride.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dualstride.data.model.*
import com.example.dualstride.data.repository.GuardianRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val vitals    : VitalData = VitalData(),
    val gait      : GaitData  = GaitData(),
    val riskLevel : RiskLevel = RiskLevel.LOW,
    val isLoading : Boolean   = true,
    val error     : String?   = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repo: GuardianRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                repo.observeVitals(),
                repo.observeGait()
            ) { vitals, gait ->
                DashboardUiState(
                    vitals = vitals,
                    gait = gait,
                    riskLevel = computeRisk(vitals, gait),
                    isLoading = false
                )
            }.catch { e ->
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}