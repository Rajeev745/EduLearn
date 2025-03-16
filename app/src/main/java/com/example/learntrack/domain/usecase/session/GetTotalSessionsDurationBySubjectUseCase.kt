package com.example.learntrack.domain.usecase.session

import com.example.learntrack.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalSessionsDurationBySubjectUseCase @Inject constructor(
    private val repository: SessionRepository
) {
    operator fun invoke(subjectId: Int): Flow<Long> {
        return repository.getTotalSessionsDurationBySubject(subjectId)
    }
}