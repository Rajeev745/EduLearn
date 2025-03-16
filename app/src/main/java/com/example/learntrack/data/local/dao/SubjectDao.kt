package com.example.learntrack.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.learntrack.data.local.entity.SubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {

    @Upsert
    suspend fun upsertSubject(subject: SubjectEntity)

    @Query("SELECT COUNT(*) FROM SUBJECTENTITY")
    fun getTotalSubjectCount(): Flow<Int>

    @Query("SELECT SUM(goalHours) FROM SUBJECTENTITY")
    fun getTotalGoalHours(): Flow<Float>

    @Query("SELECT * FROM SubjectEntity WHERE subjectId = :subjectId")
    suspend fun getSubjectById(subjectId: Int): SubjectEntity?

    @Query("DELETE FROM SubjectEntity WHERE subjectId = :subjectId")
    suspend fun deleteSubject(subjectId: Int)

    @Query("SELECT * FROM SubjectEntity")
    fun getAllSubjects(): Flow<List<SubjectEntity>>
}