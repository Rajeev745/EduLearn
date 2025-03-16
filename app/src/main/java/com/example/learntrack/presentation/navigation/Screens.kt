package com.example.learntrack.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object DashBoardScreenNavigation

@Serializable
object SessionScreenNavigation

@Serializable
data class TaskScreenNavigation(
    val taskId: Int = 0,
    val subjectId: Int = 0,
)

@Serializable
data class SubjectScreenNavigation(
    val subjectId: Int = 0,
)