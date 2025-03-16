package com.example.learntrack.di

import com.example.learntrack.domain.repository.SubjectRepository
import com.example.learntrack.domain.usecase.SubjectUseCases
import com.example.learntrack.domain.usecase.subject.DeleteSubjectUseCase
import com.example.learntrack.domain.usecase.subject.GetAllSubjectsUseCase
import com.example.learntrack.domain.usecase.subject.GetSubjectByIdUseCase
import com.example.learntrack.domain.usecase.subject.GetTotalGoalHoursUseCase
import com.example.learntrack.domain.usecase.subject.GetTotalSubjectCountUseCase
import com.example.learntrack.domain.usecase.subject.UpsertSubjectUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SubjectUseCaseModule {

    @Provides
    fun provideUpsertSubjectUseCase(repository: SubjectRepository): UpsertSubjectUseCase {
        return UpsertSubjectUseCase(repository)
    }

    @Provides
    fun provideGetTotalSubjectCountUseCase(repository: SubjectRepository): GetTotalSubjectCountUseCase {
        return GetTotalSubjectCountUseCase(repository)
    }

    @Provides
    fun provideGetTotalGoalHoursUseCase(repository: SubjectRepository): GetTotalGoalHoursUseCase {
        return GetTotalGoalHoursUseCase(repository)
    }

    @Provides
    fun provideDeleteSubjectUseCase(repository: SubjectRepository): DeleteSubjectUseCase {
        return DeleteSubjectUseCase(repository)
    }

    @Provides
    fun provideGetSubjectByIdUseCase(repository: SubjectRepository): GetSubjectByIdUseCase {
        return GetSubjectByIdUseCase(repository)
    }

    @Provides
    fun provideGetAllSubjectsUseCase(repository: SubjectRepository): GetAllSubjectsUseCase {
        return GetAllSubjectsUseCase(repository)
    }

    @Provides
    fun provideSubjectUseCases(
        deleteSubjectUseCase: DeleteSubjectUseCase,
        getAllSubjectsUseCase: GetAllSubjectsUseCase,
        getSubjectByIdUseCase: GetSubjectByIdUseCase,
        upsertSubjectUseCase: UpsertSubjectUseCase,
        getTotalGoalHoursUseCase: GetTotalGoalHoursUseCase,
        getTotalSubjectCountUseCase: GetTotalSubjectCountUseCase,
    ): SubjectUseCases {
        return SubjectUseCases(
            deleteSubjectUseCase,
            getAllSubjectsUseCase,
            getSubjectByIdUseCase,
            upsertSubjectUseCase,
            getTotalGoalHoursUseCase,
            getTotalSubjectCountUseCase
        )
    }
}
