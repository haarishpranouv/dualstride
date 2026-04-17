package com.example.dualstride.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.toReadableTime(): String =
    SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(this))

fun Long.toTimeAgo(): String {
    val diff = System.currentTimeMillis() - this
    return when {
        diff < 60_000     -> "Just now"
        diff < 3_600_000  -> "${diff / 60_000}m ago"
        diff < 86_400_000 -> "${diff / 3_600_000}h ago"
        else              -> "${diff / 86_400_000}d ago"
    }
}

fun Int.toHeartRateStatus(): String = when {
    this > 130 || this < 50 -> "CRITICAL"
    this > 110 || this < 60 -> "WARNING"
    else                    -> "NORMAL"
}

fun Int.toSpo2Status(): String = when {
    this < 92 -> "CRITICAL"
    this < 95 -> "WARNING"
    else      -> "NORMAL"
}

fun Double.toTempStatus(): String = when {
    this > 38.5 -> "CRITICAL"
    this > 37.5 -> "WARNING"
    else        -> "NORMAL"
}