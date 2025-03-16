package com.example.learntrack.domain.usecase.session

import com.example.learntrack.domain.model.Session
import com.example.learntrack.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentTenSessionsForSubjectUseCase @Inject constructor(
    private val repository: SessionRepository,
) {
    operator fun invoke(subjectId: Int): Flow<List<Session>> {
        return repository.getRecentTenSessionsForSubject(subjectId)
    }
}