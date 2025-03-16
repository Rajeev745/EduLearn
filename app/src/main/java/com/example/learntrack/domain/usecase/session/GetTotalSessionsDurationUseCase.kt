package com.example.learntrack.domain.usecase.session

import com.example.learntrack.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalSessionsDurationUseCase @Inject constructor(
    private val repository: SessionRepository,
) {
    operator fun invoke(): Flow<Long> {
        return repository.getTotalSessionsDuration()
    }
}