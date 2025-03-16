package com.example.learntrack.presentation.subject

import androidx.compose.ui.graphics.Color
import com.example.learntrack.domain.model.Session
import com.example.learntrack.domain.model.Subject
import com.example.learntrack.domain.model.Task
import com.example.learntrack.utils.StudyConstants.UtilityConstant.EMPTY_STRING

data class SubjectState(
    val currentSubjectId: Int? = null,
    val subjectName: String = EMPTY_STRING,
    val goalStudyHours: String = EMPTY_STRING,
    val subjectCardColors: List<Color> = Subject.subjectCardColors.random(),
    val studiedHours: Float = 0.0f,
    val progress: Float = 0.0f,
    val recentSessions: List<Session> = emptyList(),
    val upcomingTasks: List<Task> = emptyList(),
    val completedTasks: List<Task> = emptyList(),
    val session: Session? = null,
)