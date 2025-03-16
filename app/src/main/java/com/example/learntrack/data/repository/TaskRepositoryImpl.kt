package com.example.learntrack.data.repository

import com.example.learntrack.data.local.dao.TaskDao
import com.example.learntrack.domain.mappers.toDomain
import com.example.learntrack.domain.mappers.toEntity
import com.example.learntrack.domain.model.Task
import com.example.learntrack.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [TaskRepository] that uses Room database as a data source.
 * This class acts as a mediator between the data layer (Room database) and the domain layer.
 * It converts database entities to domain models and vice versa.
 */
class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : TaskRepository {

    /**
     * Inserts or updates a task in the database.
     * If a task with the same ID exists, it will be updated; otherwise, a new task will be created.
     *
     * @param task The task to be inserted or updated
     */
    override suspend fun upsertTask(task: Task) {
        taskDao.upsertTask(task.toEntity())
    }

    /**
     * Deletes a task from the database by its ID.
     *
     * @param taskId The ID of the task to be deleted
     */
    override suspend fun deleteTask(taskId: Int) {
        taskDao.deleteTask(taskId)
    }

    /**
     * Retrieves a specific task by its ID.
     *
     * @param taskId The ID of the task to be retrieved
     * @return The task with the specified ID, or null if not found
     */
    override suspend fun getTaskById(taskId: Int): Task? {
        return taskDao.getTaskById(taskId)?.toDomain()
    }

    /**
     * Gets a flow of upcoming tasks for a specific subject.
     *
     * @param subjectId The ID of the subject to filter tasks by
     * @return A Flow emitting a list of upcoming tasks for the specified subject
     */
    override fun getUpcomingTasksForSubject(subjectId: Int): Flow<List<Task>> {
        return taskDao.getTasksForSubject(subjectId)
            .map { taskEntity -> taskEntity.map { it.toDomain() } }
            .map { tasks -> tasks.filter { it.isComplete.not() } }
            .map { tasks -> sortTasks(tasks) }
    }

    /**
     * Gets a flow of completed tasks for a specific subject.
     *
     * @param subjectId The ID of the subject to filter tasks by
     * @return A Flow emitting a list of completed tasks for the specified subject
     */
    override fun getCompletedTasksForSubject(subjectId: Int): Flow<List<Task>> {
        return taskDao.getTasksForSubject(subjectId)
            .map { taskEntity -> taskEntity.map { it.toDomain() } }
            .map { tasks -> tasks.filter { it.isComplete } }
            .map { tasks -> sortTasks(tasks) }
    }

    /**
     * Gets a flow of all upcoming tasks across all subjects.
     *
     * @return A Flow emitting a list of all upcoming tasks
     */
    override fun getAllUpcomingTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { taskEntity ->
            taskEntity.filter { it.isComplete.not() }.map { it.toDomain() }
        }.map { task -> sortTasks(task) }
    }

    private fun sortTasks(tasksList: List<Task>): List<Task> {
        return tasksList.sortedWith(compareBy<Task> { it.dueDate }.thenByDescending { it.priority })
    }
}