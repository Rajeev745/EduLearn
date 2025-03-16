package com.example.learntrack.domain.usecase.session

import com.example.learntrack.domain.model.Session
import com.example.learntrack.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSessionsUseCase @Inject constructor(
    private val repository: SessionRepository
) {
    operator fun invoke(): Flow<List<Session>> {
        return repository.getAllSessions()
    }
}