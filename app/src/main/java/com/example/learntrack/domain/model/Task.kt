package com.example.learntrack.domain.model

data class Task(
    var title: String,
    var description: String,
    var dueDate: Long,
    var priority: Int,
    var relatedToSubject: String,
    var isComplete: Boolean,
    var taskSubjectId: Int,
    var taskId: Int? = null,
)