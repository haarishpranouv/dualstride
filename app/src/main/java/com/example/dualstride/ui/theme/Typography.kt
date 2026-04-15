package com.guardianwear.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val GuardianTypography = Typography(
    headlineLarge  = TextStyle(fontWeight = FontWeight.Bold,     fontSize = 28.sp, lineHeight = 34.sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
    titleLarge     = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
    titleMedium    = TextStyle(fontWeight = FontWeight.Medium,   fontSize = 15.sp),
    bodyLarge      = TextStyle(fontWeight = FontWeight.Normal,   fontSize = 14.sp, lineHeight = 22.sp),
    bodySmall      = TextStyle(fontWeight = FontWeight.Normal,   fontSize = 12.sp, lineHeight = 18.sp),
    labelSmall     = TextStyle(fontWeight = FontWeight.Medium,   fontSize = 10.sp, letterSpacing = 0.06.sp)
)