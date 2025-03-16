package com.example.learntrack.domain.mappers

import androidx.compose.ui.graphics.Color
import com.example.learntrack.data.local.entity.SessionEntity
import com.example.learntrack.data.local.entity.SubjectEntity
import com.example.learntrack.data.local.entity.TaskEntity
import com.example.learntrack.domain.model.Session
import com.example.learntrack.domain.model.Subject
import com.example.learntrack.domain.model.Task

// Mapper for Session class
fun SessionEntity.toDomain(): Session {
    return Session(
        sessionSubjectId = this.sessionSubjectId,
        relatedToSubject = this.relatedToSubject,
        date = this.date,
        duration = this.duration,
        sessionId = this.sessionId ?: 0 // Default to 0 if null
    )
}

fun Session.toEntity(): SessionEntity {
    return SessionEntity(
        sessionSubjectId = this.sessionSubjectId,
        relatedToSubject = this.relatedToSubject,
        date = this.date,
        duration = this.duration,
        sessionId = if (this.sessionId == 0) null else this.sessionId // Handle null for auto-generated ID
    )
}


// Mapper for subject class
fun SubjectEntity.toDomain(): Subject {
    return Subject(
        name = this.name,
        goalHours = this.goalHours,
        colors = this.colors,
        subjectId = this.subjectId ?: 0 // Default to 0 if null
    )
}

fun Subject.toEntity(): SubjectEntity {
    return SubjectEntity(
        name = this.name,
        goalHours = this.goalHours,
        colors = this.colors,
        subjectId = if (this.subjectId == 0) null else this.subjectId // Handle null for auto-generated ID
    )
}

// Mapper for Task class
fun TaskEntity.toDomain(): Task {
    return Task(
        title = this.title,
        description = this.description,
        dueDate = this.dueDate,
        priority = this.priority,
        relatedToSubject = this.relatedToSubject,
        isComplete = this.isComplete,
        taskSubjectId = this.taskSubjectId,
        taskId = this.taskId ?: 0 // Default to 0 if null
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        title = this.title,
        description = this.description,
        dueDate = this.dueDate,
        priority = this.priority,
        relatedToSubject = this.relatedToSubject,
        isComplete = this.isComplete,
        taskSubjectId = this.taskSubjectId,
        taskId = if (this.taskId == 0) null else this.taskId // Handle null for auto-generated ID
    )
}

