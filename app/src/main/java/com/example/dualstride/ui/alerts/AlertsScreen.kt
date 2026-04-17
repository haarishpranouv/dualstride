package com.guardianwear.app.ui.alerts

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guardianwear.app.data.model.AlertEvent
import com.guardianwear.app.ui.theme.*
import com.guardianwear.app.util.toTimeAgo

@Composable
fun AlertsScreen(viewModel: AlertsViewModel = hiltViewModel()) {
    val state  by viewModel.uiState.collectAsStateWithLifecycle()
    var filter by remember { mutableStateOf("ALL") }
    val filters = listOf("ALL", "CRITICAL", "HIGH", "MEDIUM", "LOW")

    Column(modifier = Modifier.fillMaxSize().background(BgPrimary)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Alerts", style = MaterialTheme.typography.titleLarge,
                color = TextPrimary, fontWeight = FontWeight.Bold)
            Text("${state.alerts.count { !it.resolved }} unresolved",
                style = MaterialTheme.typography.bodySmall, color = TextMuted)
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.horizontalScroll(rememberScrollState())) {
                filters.forEach { f ->
                    val selected = filter == f
                    FilterChip(
                        selected = selected, onClick = { filter = f },
                        label    = { Text(f, fontSize = 11.sp) },
                        colors   = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0x202DD4BF),
                            selectedLabelColor     = Teal400,
                            containerColor         = BgSurface2,
                            labelColor             = TextMuted
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true, selected = selected,
                            selectedBorderColor = Teal400, borderColor = BorderColor,
                            borderWidth = 0.5.dp, selectedBorderWidth = 1.dp
                        )
                    )
                }
            }
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Teal400)
            }
        } else {
            val filtered = if (filter == "ALL") state.alerts
            else state.alerts.filter { it.severity == filter }
            if (filtered.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("✅", fontSize = 40.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("No alerts", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filtered, key = { it.id }) { alert ->
                        AlertCard(alert = alert, onResolve = { viewModel.resolveAlert(alert.id) })
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
fun AlertCard(alert: AlertEvent, onResolve: () -> Unit) {
    val (color, bg, emoji) = when (alert.severity) {
        "CRITICAL" -> Triple(Red400,   Color(0x1AF87171), "🆘")
        "HIGH"     -> Triple(Red400,   Color(0x12F87171), "🚨")
        "MEDIUM"   -> Triple(Amber400, Color(0x1AFBBF24), "⚠️")
        else       -> Triple(Green400, Color(0x1A4ADE80), "ℹ️")
    }
    val typeLabel = when (alert.type) {
        "FALL"        -> "Fall Detected"
        "CARDIAC"     -> "Cardiac Anomaly"
        "DEHYDRATION" -> "Dehydration Risk"
        "GAIT"        -> "Gait Imbalance"
        else          -> alert.type
    }
    Row(modifier = Modifier.fillMaxWidth()
        .background(if (alert.resolved) BgSurface else bg, RoundedCornerShape(14.dp))
        .border(1.dp, if (alert.resolved) BorderColor else color.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
        .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.size(42.dp)
            .background(if (alert.resolved) BgSurface3 else bg, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center) {
            Text(if (alert.resolved) "✅" else emoji, fontSize = 18.sp)
        }
        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(typeLabel, fontWeight = FontWeight.SemiBold, fontSize = 13.sp,
                    color = if (alert.resolved) TextMuted else color)
                SeverityBadge(severity = alert.severity, resolved = alert.resolved)
            }
            Spacer(Modifier.height(4.dp))
            Text(alert.message, style = MaterialTheme.typography.bodySmall,
                color = if (alert.resolved) TextMuted else TextPrimary)
            Spacer(Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(alert.timestamp.toTimeAgo(), style = MaterialTheme.typography.labelSmall, color = TextMuted)
                if (!alert.resolved) {
                    TextButton(onClick = onResolve,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)) {
                        Text("Resolve", color = Teal400, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SeverityBadge(severity: String, resolved: Boolean) {
    val (color, bg) = if (resolved) Pair(TextMuted, BgSurface3) else when (severity) {
        "CRITICAL" -> Pair(Red400,   Color(0x1AF87171))
        "HIGH"     -> Pair(Red400,   Color(0x12F87171))
        "MEDIUM"   -> Pair(Amber400, Color(0x1AFBBF24))
        else       -> Pair(Green400, Color(0x1A4ADE80))
    }
    Text(
        text = if (resolved) "RESOLVED" else severity,
        modifier = Modifier.background(bg, RoundedCornerShape(6.dp))
            .padding(horizontal = 7.dp, vertical = 2.dp),
        color = color, fontSize = 9.sp, fontWeight = FontWeight.Bold
    )
}