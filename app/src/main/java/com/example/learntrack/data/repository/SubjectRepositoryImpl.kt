package com.example.learntrack.data.repository

import com.example.learntrack.data.local.dao.SessionDao
import com.example.learntrack.data.local.dao.SubjectDao
import com.example.learntrack.data.local.dao.TaskDao
import com.example.learntrack.domain.mappers.toDomain
import com.example.learntrack.domain.mappers.toEntity
import com.example.learntrack.domain.model.Subject
import com.example.learntrack.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [SubjectRepository] that uses a [SubjectDao] as its data source.
 * This repository is responsible for mapping data between domain and database representations
 * and providing access to subject-related operations.
 */
class SubjectRepositoryImpl @Inject constructor(
    private val subjectDao: SubjectDao,
    private val sessionDao: SessionDao,
    private val taskDao: TaskDao,
) :
    SubjectRepository {

    /**
     * Inserts or updates a subject in the database.
     *
     * @param subject The subject to be inserted or updated.
     */
    override suspend fun upsertSubject(subject: Subject) {
        subjectDao.upsertSubject(subject.toEntity())
    }

    /**
     * Gets the total count of subjects in the database as a Flow.
     *
     * @return A Flow emitting the count of subjects.
     */
    override fun getTotalSubjectCount(): Flow<Int> {
        return subjectDao.getTotalSubjectCount()
    }

    /**
     * Gets the total goal hours across all subjects as a Flow.
     *
     * @return A Flow emitting the sum of goal hours.
     */
    override fun getTotalGoalHours(): Flow<Float> {
        return subjectDao.getTotalGoalHours()
    }

    /**
     * Deletes a subject from the database by its ID.
     *
     * @param subjectId The ID of the subject to delete.
     */
    override suspend fun deleteSubject(subjectId: Int) {
        subjectDao.deleteSubject(subjectId)
        taskDao.deleteTasksBySubjectId(subjectId)
        sessionDao.deleteSessionsBySubjectId(subjectId)
    }

    /**
     * Retrieves a subject by its ID.
     *
     * @param subjectId The ID of the subject to retrieve.
     * @return The subject if found, null otherwise.
     */
    override suspend fun getSubjectById(subjectId: Int): Subject? {
        return subjectDao.getSubjectById(subjectId)?.toDomain()
    }

    /**
     * Gets all subjects from the database as a Flow.
     *
     * @return A Flow emitting a list of all subjects.
     */
    override fun getAllSubjects(): Flow<List<Subject>> {
        return subjectDao.getAllSubjects()
            .map { subjectEntity -> subjectEntity.map { it.toDomain() } }
    }
}