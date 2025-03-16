package com.example.learntrack.presentation.task

import com.example.learntrack.domain.model.Subject
import com.example.learntrack.utils.SubjectPriority

/**
 * Sealed class representing all UI events related to task management.
 * These events are sent from the UI layer to the ViewModel to indicate user actions.
 */
sealed class OnTaskEvent {
    
    data class OnTitleChange(val title: String) : OnTaskEvent()
    data class OnDescriptionChange(val description: String) : OnTaskEvent()
    data class OnDateChange(val millis: Long?) : OnTaskEvent()
    data class OnPriorityChange(val priority: SubjectPriority) : OnTaskEvent()
    data class OnRelatedSubjectSelect(val subject: Subject) : OnTaskEvent()
    data object OnIsCompleteChange : OnTaskEvent()
    data object SaveTask : OnTaskEvent()
    data object DeleteTask : OnTaskEvent()
    
}