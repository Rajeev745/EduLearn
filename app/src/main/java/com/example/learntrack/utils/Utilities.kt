package com.example.learntrack.utils

import androidx.compose.material3.SnackbarDuration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long?.changeMillisToDateString(): String {
    val date: LocalDate = this?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
    } ?: LocalDate.now()

    return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
}

fun Long.toHours(): Float {
    val hours = this.toFloat() / 3600f
    return "%.2f".format(hours).toFloat()
}

sealed class SnackBarEvents {

    data class ShowSnackBar(
        val message: String,
        val duration: SnackbarDuration = SnackbarDuration.Short,
    ) : SnackBarEvents()

    data object NavigateUp : SnackBarEvents()
}