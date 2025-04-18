package com.example.learntrack.data.local.entity

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.learntrack.presentation.theme.gradient1
import com.example.learntrack.presentation.theme.gradient2
import com.example.learntrack.presentation.theme.gradient3
import com.example.learntrack.presentation.theme.gradient4
import com.example.learntrack.presentation.theme.gradient5

@Entity
data class SubjectEntity(
    val name: String,
    val goalHours: Float,
    val colors: List<Int>,
    @PrimaryKey(autoGenerate = true) val subjectId: Int? = null,
) {
    companion object {
        val subjectCardColors = listOf(gradient1, gradient2, gradient3, gradient4, gradient5)
    }
}