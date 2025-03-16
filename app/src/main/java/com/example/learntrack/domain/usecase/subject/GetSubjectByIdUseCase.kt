package com.example.learntrack.domain.usecase.subject

import com.example.learntrack.domain.model.Subject
import com.example.learntrack.domain.repository.SubjectRepository
import javax.inject.Inject

class GetSubjectByIdUseCase @Inject constructor(private val repository: SubjectRepository) {

    suspend operator fun invoke(subjectId: Int): Subject? {
        return repository.getSubjectById(subjectId)
    }
}