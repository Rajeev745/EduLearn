package com.example.learntrack.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.learntrack.data.local.entity.SessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert
    suspend fun insertSession(session: SessionEntity)

    @Delete
    suspend fun deleteSession(session: SessionEntity)

    @Query("SELECT * FROM SessionEntity")
    fun getAllSessions(): Flow<List<SessionEntity>>

    @Query("SELECT * FROM SessionEntity WHERE sessionSubjectId = :subjectId")
    fun getRecentSessionsForSubject(subjectId: Int): Flow<List<SessionEntity>>

    @Query("SELECT SUM(duration) FROM SessionEntity")
    fun getTotalSessionsDuration(): Flow<Long>

    @Query("SELECT SUM(duration) FROM SessionEntity WHERE sessionSubjectId = :subjectId")
    fun getTotalSessionsDurationBySubject(subjectId: Int): Flow<Long>

    @Query("DELETE FROM SessionEntity WHERE sessionSubjectId = :subjectId")
    fun deleteSessionsBySubjectId(subjectId: Int)
}