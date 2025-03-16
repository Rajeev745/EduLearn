package com.example.learntrack.domain.usecase.task

import com.example.learntrack.domain.model.Task
import com.example.learntrack.domain.repository.TaskRepository
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(taskId: Int): Task? {
        return repository.getTaskById(taskId)
    }
}