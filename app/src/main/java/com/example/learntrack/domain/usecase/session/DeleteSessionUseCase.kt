package com.example.learntrack.domain.usecase.session

import com.example.learntrack.domain.model.Session
import com.example.learntrack.domain.repository.SessionRepository
import javax.inject.Inject

class DeleteSessionUseCase @Inject constructor(
    private val repository: SessionRepository
) {
    suspend operator fun invoke(session: Session) {
        repository.deleteSession(session)
    }
}