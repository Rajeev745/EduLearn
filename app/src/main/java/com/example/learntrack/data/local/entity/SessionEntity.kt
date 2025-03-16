package com.example.learntrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SessionEntity(
    val sessionSubjectId: Int,
    val relatedToSubject: String,
    val date: Long,
    val duration: Long,
    @PrimaryKey(autoGenerate = true) val sessionId: Int? = null,
)