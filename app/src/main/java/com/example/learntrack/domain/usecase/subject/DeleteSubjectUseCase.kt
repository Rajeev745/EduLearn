package com.example.learntrack.domain.usecase.subject

import com.example.learntrack.domain.repository.SubjectRepository
import javax.inject.Inject

class DeleteSubjectUseCase @Inject constructor(private val repository: SubjectRepository) {

    suspend operator fun invoke(subjectId: Int) {
        repository.deleteSubject(subjectId)
    }
}