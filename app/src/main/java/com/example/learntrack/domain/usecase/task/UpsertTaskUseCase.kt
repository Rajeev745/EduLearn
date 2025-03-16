package com.example.learntrack.domain.usecase.task

import com.example.learntrack.domain.model.Task
import com.example.learntrack.domain.repository.TaskRepository
import javax.inject.Inject

class UpsertTaskUseCase @Inject constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(task: Task) {
        repository.upsertTask(task)
    }
}