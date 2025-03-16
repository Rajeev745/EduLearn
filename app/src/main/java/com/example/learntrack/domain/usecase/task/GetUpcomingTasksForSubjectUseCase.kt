package com.example.learntrack.domain.usecase.task

import com.example.learntrack.domain.model.Task
import com.example.learntrack.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUpcomingTasksForSubjectUseCase @Inject constructor(private val repository: TaskRepository) {

    operator fun invoke(subjectId: Int): Flow<List<Task>> {
        return repository.getUpcomingTasksForSubject(subjectId)
    }
}