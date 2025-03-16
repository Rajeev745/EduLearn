package com.example.learntrack.domain.usecase.subject

import com.example.learntrack.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalGoalHoursUseCase @Inject constructor(private val repository: SubjectRepository) {

    operator fun invoke(): Flow<Float> {
        return repository.getTotalGoalHours()
    }
}