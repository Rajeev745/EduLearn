package com.example.learntrack.data.repository

import com.example.learntrack.data.local.dao.SessionDao
import com.example.learntrack.domain.mappers.toDomain
import com.example.learntrack.domain.mappers.toEntity
import com.example.learntrack.domain.model.Session
import com.example.learntrack.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of SessionRepository that uses SessionDao to interact with the local database.
 * This class handles all database operations related to study sessions including CRUD operations
 * and queries for statistics.
 */
class SessionRepositoryImpl @Inject constructor(private val sessionDao: SessionDao) :
    SessionRepository {

    /**
     * Inserts a new session into the database.
     * @param session The session domain model to be inserted
     */
    override suspend fun insertSession(session: Session) {
        sessionDao.insertSession(session.toEntity())
    }

    /**
     * Deletes an existing session from the database.
     * @param session The session domain model to be deleted
     */
    override suspend fun deleteSession(session: Session) {
        sessionDao.deleteSession(session.toEntity())
    }

    /**
     * Retrieves all sessions from the database.
     * @return A Flow emitting a list of all sessions as domain models
     */
    override fun getAllSessions(): Flow<List<Session>> {
        return sessionDao.getAllSessions().map { entityList -> entityList.map { it.toDomain() } }
    }

    /**
     * Retrieves the five most recent sessions based on date.
     * @return A Flow emitting a list of the five most recent sessions as domain models
     */
    override fun getRecentFiveSessions(): Flow<List<Session>> {
        return sessionDao.getAllSessions()
            .map { sessions ->
                sessions.sortedByDescending { it.date }
                    .take(5)
                    .map { it.toDomain() }
            }
    }

    /**
     * Retrieves the ten most recent sessions for a specific subject based on date.
     * @param subjectId The ID of the subject to filter sessions by
     * @return A Flow emitting a list of the ten most recent sessions for the subject as domain models
     */
    override fun getRecentTenSessionsForSubject(subjectId: Int): Flow<List<Session>> {
        return sessionDao.getAllSessions()
            .map { sessions ->
                sessions.sortedByDescending { it.date }
                    .take(10)
                    .map { it.toDomain() }
            }
    }

    /**
     * Retrieves the total duration of all sessions.
     * @return A Flow emitting the sum of all session durations in milliseconds
     */
    override fun getTotalSessionsDuration(): Flow<Long> {
        return sessionDao.getTotalSessionsDuration()
    }

    /**
     * Retrieves the total duration of all sessions for a specific subject.
     * @param subjectId The ID of the subject to calculate total duration for
     * @return A Flow emitting the sum of session durations for the subject in milliseconds
     */
    override fun getTotalSessionsDurationBySubject(subjectId: Int): Flow<Long> {
        return sessionDao.getTotalSessionsDurationBySubject(subjectId)
    }
}