package com.example.learntrack.utils

import androidx.compose.ui.graphics.Color
import com.example.learntrack.presentation.theme.Green
import com.example.learntrack.presentation.theme.Orange
import com.example.learntrack.presentation.theme.Red

enum class SubjectPriority(val value: Int, val color: Color, val title: String) {
    LOW(0, Green, "Low"),
    MEDIUM(1, Orange, "Medium"),
    HIGH(2, Red, "High");

    companion object {
        fun fromInt(value: Int) = entries.firstOrNull { it.value == value } ?: MEDIUM
    }
}