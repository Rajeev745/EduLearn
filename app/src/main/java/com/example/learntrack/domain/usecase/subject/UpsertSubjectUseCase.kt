package com.example.learntrack.domain.usecase.subject

import com.example.learntrack.domain.model.Subject
import com.example.learntrack.domain.repository.SubjectRepository
import javax.inject.Inject

class UpsertSubjectUseCase @Inject constructor(private val repository: SubjectRepository) {

    suspend operator fun invoke(subject: Subject) {
        repository.upsertSubject(subject)
    }
}