package com.example.learntrack.presentation.dashboard

import androidx.compose.ui.graphics.Color
import com.example.learntrack.domain.model.Session
import com.example.learntrack.domain.model.Subject
import com.example.learntrack.utils.StudyConstants.UtilityConstant.EMPTY_STRING

data class DashboardState(
    val totalSubjectCount: Int = 0,
    val totalStudiedHours: Float = 0f,
    val totalGoalStudyHours: Float = 0f,
    val subjects: List<Subject> = emptyList(),
    val subjectName: String = EMPTY_STRING,
    val goalStudyHours: String = EMPTY_STRING,
    val subjectCardColors: List<Color> = Subject.subjectCardColors.random(),
    val session: Session? = null,
)