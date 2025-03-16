package com.example.learntrack.domain.usecase.task

import com.example.learntrack.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(taskId: Int) {
        repository.deleteTask(taskId)
    }
}