package com.example.learntrack.domain.usecase.task

import com.example.learntrack.domain.model.Task
import com.example.learntrack.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllUpcomingTasksUseCase @Inject constructor(private val repository: TaskRepository) {

    operator fun invoke(): Flow<List<Task>> {
        return repository.getAllUpcomingTasks()
    }
}