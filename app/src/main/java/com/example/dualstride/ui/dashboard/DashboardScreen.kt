package com.guardianwear.app.ui.dashboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guardianwear.app.data.model.*
import com.guardianwear.app.ui.theme.*

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("Dashboard", style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary, fontWeight = FontWeight.Bold)
                Text("Live health monitoring", style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
            LiveBadge()
        }

        Spacer(Modifier.height(16.dp))
        RiskBanner(riskLevel = state.riskLevel)
        Spacer(Modifier.height(16.dp))

        SectionLabel("Vital Signs")
        Spacer(Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            VitalCard(Modifier.weight(1f), "Heart Rate", "${state.vitals.heartRate}", "bpm",
                Icons.Default.Favorite, heartRateColor(state.vitals.heartRate))
            VitalCard(Modifier.weight(1f), "SpO₂", "${state.vitals.spo2}", "%",
                Icons.Default.Air, spo2Color(state.vitals.spo2))
        }
        Spacer(Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            VitalCard(Modifier.weight(1f), "Skin Temp",
                String.format("%.1f", state.vitals.skinTemp), "°C",
                Icons.Default.Thermostat, tempColor(state.vitals.skinTemp))
            VitalCard(Modifier.weight(1f), "ECG",
                String.format("%.2f", state.vitals.ecgValue), "mV",
                Icons.Default.MonitorHeart, Teal400)
        }

        Spacer(Modifier.height(16.dp))
        SectionLabel("Gait & Balance")
        Spacer(Modifier.height(10.dp))
        GaitPanel(gait = state.gait)

        Spacer(Modifier.height(16.dp))
        SectionLabel("Activity Today")
        Spacer(Modifier.height(10.dp))
        StepCountCard(steps = state.gait.stepCount)
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun LiveBadge() {
    val alpha by rememberInfiniteTransition(label = "blink").animateFloat(
        initialValue  = 1f, targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse), label = "alpha"
    )
    Row(
        modifier = Modifier
            .background(Color(0x1A4ADE80), RoundedCornerShape(20.dp))
            .border(1.dp, Color(0x334ADE80), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(modifier = Modifier.size(7.dp)
            .background(Green400.copy(alpha = alpha), RoundedCornerShape(50)))
        Text("LIVE", fontSize = 10.sp, color = Green400, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun RiskBanner(riskLevel: RiskLevel) {
    val (color, bg, label, icon) = when (riskLevel) {
        RiskLevel.LOW      -> Quad(Green400, Color(0x1A4ADE80), "Low Risk — All vitals normal",       "✅")
        RiskLevel.MEDIUM   -> Quad(Amber400, Color(0x1AFBBF24), "Medium Risk — Monitor closely",      "⚠️")
        RiskLevel.HIGH     -> Quad(Red400,   Color(0x1AF87171), "High Risk — Caregiver notified",     "🚨")
        RiskLevel.CRITICAL -> Quad(Red400,   Color(0x33F87171), "CRITICAL — Emergency SOS sent",     "🆘")
    }
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(bg, RoundedCornerShape(12.dp))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(icon, fontSize = 22.sp)
        Column(modifier = Modifier.weight(1f)) {
            Text(label, color = color, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text("Risk level: ${riskLevel.name}", color = TextMuted,
                style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun VitalCard(modifier: Modifier, label: String, value: String,
              unit: String, icon: ImageVector, statusColor: Color) {
    Column(modifier = modifier
        .background(BgSurface2, RoundedCornerShape(14.dp))
        .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
        .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Icon(icon, null, tint = statusColor, modifier = Modifier.size(16.dp))
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(value, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = statusColor)
            Text(unit, style = MaterialTheme.typography.bodySmall, color = TextMuted,
                modifier = Modifier.padding(bottom = 4.dp))
        }
        Spacer(Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth().height(3.dp)
            .background(BgSurface3, RoundedCornerShape(2.dp))) {
            Box(modifier = Modifier.fillMaxWidth(0.7f).height(3.dp)
                .background(statusColor, RoundedCornerShape(2.dp)))
        }
    }
}

@Composable
fun GaitPanel(gait: GaitData) {
    Column(modifier = Modifier.fillMaxWidth()
        .background(BgSurface2, RoundedCornerShape(14.dp))
        .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        GaitRow("Gait Symmetry", gait.symmetryIndex, "%",    gaitColor(gait.symmetryIndex))
        GaitRow("Balance Score", gait.balanceScore,  "/ 100",gaitColor(gait.balanceScore))
        GaitRow("Stride Length", gait.strideLength.toInt(), "cm", Teal400)
        Divider(color = BorderColor, thickness = 0.5.dp)
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text("Fall Risk", style = MaterialTheme.typography.bodySmall, color = TextMuted)
            val rc = when (gait.fallRisk) { "HIGH" -> Red400; "MEDIUM" -> Amber400; else -> Green400 }
            Text(gait.fallRisk, color = rc, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        }
    }
}

@Composable
fun GaitRow(label: String, value: Int, unit: String, color: Color) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = TextMuted)
            Text("$value $unit", style = MaterialTheme.typography.bodySmall,
                color = color, fontWeight = FontWeight.Medium)
        }
        Spacer(Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth().height(5.dp)
            .background(BgSurface3, RoundedCornerShape(3.dp))) {
            Box(modifier = Modifier.fillMaxWidth(value / 100f).height(5.dp)
                .background(color, RoundedCornerShape(3.dp)))
        }
    }
}

@Composable
fun StepCountCard(steps: Int) {
    Row(modifier = Modifier.fillMaxWidth()
        .background(BgSurface2, RoundedCornerShape(14.dp))
        .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
        .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(modifier = Modifier.size(44.dp)
                .background(Color(0x1A2DD4BF), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center) { Text("👟", fontSize = 20.sp) }
            Column {
                Text("Steps Today", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                Text("%,d".format(steps), fontSize = 22.sp,
                    fontWeight = FontWeight.Bold, color = TextPrimary)
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("Goal: 5,000", style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Text("${(steps / 5000f * 100).toInt().coerceAtMost(100)}%",
                color = Teal400, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        }
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(text.uppercase(), style = MaterialTheme.typography.labelSmall,
        color = TextMuted, fontWeight = FontWeight.Medium, letterSpacing = 1.sp)
}

fun heartRateColor(hr: Int)    = if (hr > 130 || hr < 50) Red400 else if (hr > 110) Amber400 else Teal400
fun spo2Color(spo2: Int)       = if (spo2 < 92) Red400 else if (spo2 < 95) Amber400 else Teal400
fun tempColor(temp: Double)    = if (temp > 38.5) Red400 else if (temp > 37.5) Amber400 else Teal400
fun gaitColor(value: Int)      = if (value < 60) Red400 else if (value < 75) Amber400 else Teal400

data class Quad<A,B,C,D>(val a:A, val b:B, val c:C, val d:D)
private operator fun <A,B,C,D> Quad<A,B,C,D>.component1() = a
private operator fun <A,B,C,D> Quad<A,B,C,D>.component2() = b
private operator fun <A,B,C,D> Quad<A,B,C,D>.component3() = c
private operator fun <A,B,C,D> Quad<A,B,C,D>.component4() = d