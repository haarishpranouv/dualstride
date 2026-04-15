package com.guardianwear.app.data.model

data class VitalData(
    val heartRate : Int    = 0,
    val spo2      : Int    = 0,
    val skinTemp  : Double = 0.0,
    val ecgValue  : Double = 0.0,
    val timestamp : Long   = 0L
)

data class GaitData(
    val symmetryIndex : Int    = 0,
    val balanceScore  : Int    = 0,
    val stepCount     : Int    = 0,
    val strideLength  : Double = 0.0,
    val fallRisk      : String = "LOW",
    val timestamp     : Long   = 0L
)

data class AlertEvent(
    val id        : String  = "",
    val type      : String  = "",
    val severity  : String  = "LOW",
    val message   : String  = "",
    val resolved  : Boolean = false,
    val timestamp : Long    = 0L
)

data class UserProfile(
    val uid            : String       = "",
    val name           : String       = "",
    val age            : Int          = 0,
    val bloodGroup     : String       = "",
    val emergencyName  : String       = "",
    val emergencyPhone : String       = "",
    val conditions     : List<String> = emptyList(),
    val fcmToken       : String       = ""
)

enum class RiskLevel { LOW, MEDIUM, HIGH, CRITICAL }

fun computeRisk(vitals: VitalData, gait: GaitData): RiskLevel {
    var score = 0
    if (vitals.heartRate > 130 || vitals.heartRate < 50) score += 3
    else if (vitals.heartRate > 110) score += 1
    if (vitals.spo2 < 92) score += 3
    else if (vitals.spo2 < 95) score += 1
    if (vitals.skinTemp > 38.5) score += 2
    if (gait.balanceScore < 60) score += 3
    else if (gait.balanceScore < 75) score += 1
    if (gait.symmetryIndex < 70) score += 2
    return when {
        score >= 6 -> RiskLevel.CRITICAL
        score >= 4 -> RiskLevel.HIGH
        score >= 2 -> RiskLevel.MEDIUM
        else       -> RiskLevel.LOW
    }
}