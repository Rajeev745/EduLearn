package com.example.learntrack.domain.model

import com.example.learntrack.presentation.theme.gradient1
import com.example.learntrack.presentation.theme.gradient2
import com.example.learntrack.presentation.theme.gradient3
import com.example.learntrack.presentation.theme.gradient4
import com.example.learntrack.presentation.theme.gradient5

data class Subject(
    val name: String,
    val goalHours: Float,
    val colors: List<Int>,
    val subjectId: Int? = null,
) {
    companion object {
        val subjectCardColors = listOf(gradient1, gradient2, gradient3, gradient4, gradient5)
    }
}