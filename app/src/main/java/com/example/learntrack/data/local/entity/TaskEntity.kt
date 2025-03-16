package com.example.learntrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(
    var title: String,
    var description: String,
    var dueDate: Long,
    var priority: Int,
    var relatedToSubject: String,
    var isComplete: Boolean,
    var taskSubjectId: Int,
    @PrimaryKey(autoGenerate = true) var taskId: Int? = null,
)