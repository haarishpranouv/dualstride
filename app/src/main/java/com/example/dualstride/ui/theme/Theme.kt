package com.guardianwear.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Colors ────────────────────────────────────────────────────────────────────
val Teal400     = Color(0xFF2DD4BF)
val Teal900     = Color(0xFF0D3D36)
val Amber400    = Color(0xFFFBBF24)
val Red400      = Color(0xFFF87171)
val Blue400     = Color(0xFF60A5FA)
val Green400    = Color(0xFF4ADE80)
val BgPrimary   = Color(0xFF060910)
val BgSurface   = Color(0xFF0C1018)
val BgSurface2  = Color(0xFF111520)
val BgSurface3  = Color(0xFF171D2A)
val TextPrimary = Color(0xFFDDE4F0)
val TextMuted   = Color(0xFF6B7690)
val BorderColor = Color(0xFF1E2535)

// ── Dark color scheme ─────────────────────────────────────────────────────────
private val DarkColorScheme = darkColorScheme(
    primary          = Teal400,
    onPrimary        = Color(0xFF003731),
    primaryContainer = Teal900,
    secondary        = Blue400,
    tertiary         = Amber400,
    background       = BgPrimary,
    surface          = BgSurface,
    surfaceVariant   = BgSurface2,
    onBackground     = TextPrimary,
    onSurface        = TextPrimary,
    onSurfaceVariant = TextMuted,
    error            = Red400,
    outline          = BorderColor
)

// ── Theme ─────────────────────────────────────────────────────────────────────
@Composable
fun GuardianWearTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography  = GuardianTypography,
        content     = content
    )
}