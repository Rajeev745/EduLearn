package com.example.learntrack.presentation.subject

import androidx.compose.ui.graphics.Color
import com.example.learntrack.domain.model.Session
import com.example.learntrack.domain.model.Task

sealed class OnSubjectEvent {

    data object UpdateSubject : OnSubjectEvent()
    data object DeleteSubject : OnSubjectEvent()
    data object DeleteSession : OnSubjectEvent()
    data object UpdateProgress : OnSubjectEvent()
    data class OnTaskIsCompleteChange(val task: Task): OnSubjectEvent()
    data class OnSubjectCardColorChange(val color: List<Color>): OnSubjectEvent()
    data class OnSubjectNameChange(val name: String): OnSubjectEvent()
    data class OnGoalStudyHoursChange(val hours: String): OnSubjectEvent()
    data class OnDeleteSessionButtonClick(val session: Session): OnSubjectEvent()
}