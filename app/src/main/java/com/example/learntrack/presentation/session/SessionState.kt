package com.example.learntrack.presentation.session

import com.example.learntrack.domain.model.Session
import com.example.learntrack.domain.model.Subject

data class SessionState(
    val subjects: List<Subject> = emptyList(),
    val sessions: List<Session> = emptyList(),
    val relatedToSubject: String? = null,
    val subjectId: Int? = null,
    val session: Session? = null
)