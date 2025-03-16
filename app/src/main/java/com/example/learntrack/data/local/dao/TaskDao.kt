package com.example.learntrack.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.learntrack.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Upsert
    suspend fun upsertTask(task: TaskEntity)

    @Query("DELETE FROM TaskEntity WHERE taskId = :taskId")
    suspend fun deleteTask(taskId: Int)

    @Query("DELETE FROM TaskEntity WHERE taskSubjectId = :subjectId")
    suspend fun deleteTasksBySubjectId(subjectId: Int)

    @Query("SELECT * FROM TaskEntity WHERE taskId = :taskId")
    suspend fun getTaskById(taskId: Int): TaskEntity?

    @Query("SELECT * FROM TaskEntity WHERE taskSubjectId = :subjectId")
    fun getTasksForSubject(subjectId: Int): Flow<List<TaskEntity>>

    @Query("SELECT * FROM TaskEntity")
    fun getAllTasks(): Flow<List<TaskEntity>>
}