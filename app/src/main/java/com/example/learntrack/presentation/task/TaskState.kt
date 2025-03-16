package com.example.learntrack.presentation.task

import com.example.learntrack.domain.model.Subject
import com.example.learntrack.utils.StudyConstants.UtilityConstant.EMPTY_STRING
import com.example.learntrack.utils.SubjectPriority

/**
 * Represents the state of a task in the application.
 * This data class holds all the necessary information about a task including
 * its title, description, due date, completion status, priority level,
 * related subject information, and task identifiers.
 * 
 * @property title The title of the task
 * @property description The detailed description of the task
 * @property dueDate The due date of the task represented as a timestamp
 * @property isCompleted Whether the task has been completed
 * @property priority The priority level of the task
 * @property relatedToSubject The name of the related subject if any
 * @property subjects List of all subjects for selection
 * @property subjectId The ID of the subject this task belongs to
 * @property currentTaskId The unique identifier for this task
 */
data class TaskState(
    val title: String = EMPTY_STRING,
    val description: String = EMPTY_STRING,
    val dueDate: Long? = null,
    val isCompleted: Boolean = false,
    val priority: SubjectPriority = SubjectPriority.LOW,
    val relatedToSubject: String? = null,
    val subjects: List<Subject> = emptyList(),
    val subjectId: Int? = null,
    val currentTaskId: Int? = null,
)