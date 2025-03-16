package com.example.learntrack.domain.usecase.subject

import com.example.learntrack.domain.model.Subject
import com.example.learntrack.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSubjectsUseCase @Inject constructor(private val repository: SubjectRepository) {

    fun invoke(): Flow<List<Subject>> {
        return repository.getAllSubjects()
    }
}