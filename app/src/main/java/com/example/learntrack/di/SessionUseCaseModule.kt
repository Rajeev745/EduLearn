package com.example.learntrack.di

import com.example.learntrack.domain.repository.SessionRepository
import com.example.learntrack.domain.usecase.SessionUseCases
import com.example.learntrack.domain.usecase.session.AddSessionUseCase
import com.example.learntrack.domain.usecase.session.DeleteSessionUseCase
import com.example.learntrack.domain.usecase.session.GetAllSessionsUseCase
import com.example.learntrack.domain.usecase.session.GetRecentFiveSessionsUseCase
import com.example.learntrack.domain.usecase.session.GetRecentTenSessionsForSubjectUseCase
import com.example.learntrack.domain.usecase.session.GetTotalSessionsDurationBySubjectUseCase
import com.example.learntrack.domain.usecase.session.GetTotalSessionsDurationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionUseCaseModule {

    @Provides
    fun provideInsertSessionUseCase(repository: SessionRepository): AddSessionUseCase {
        return AddSessionUseCase(repository)
    }

    @Provides
    fun provideDeleteSessionUseCase(
        repository: SessionRepository,
    ): DeleteSessionUseCase {
        return DeleteSessionUseCase(repository)
    }

    @Provides
    fun provideGetAllSessionsUseCase(
        repository: SessionRepository,
    ): GetAllSessionsUseCase {
        return GetAllSessionsUseCase(repository)
    }

    @Provides
    fun provideGetRecentFiveSessionsUseCase(
        repository: SessionRepository,
    ): GetRecentFiveSessionsUseCase {
        return GetRecentFiveSessionsUseCase(repository)
    }

    @Provides
    fun provideGetRecentTenSessionsForSubjectUseCase(
        repository: SessionRepository,
    ): GetRecentTenSessionsForSubjectUseCase {
        return GetRecentTenSessionsForSubjectUseCase(repository)
    }

    @Provides
    fun provideGetTotalSessionsDurationUseCase(
        repository: SessionRepository,
    ): GetTotalSessionsDurationUseCase {
        return GetTotalSessionsDurationUseCase(repository)
    }

    @Provides
    fun provideGetTotalSessionsDurationBySubjectUseCase(
        repository: SessionRepository,
    ): GetTotalSessionsDurationBySubjectUseCase {
        return GetTotalSessionsDurationBySubjectUseCase(repository)
    }

    @Provides
    fun provideSessionUseCases(
        addSessionUseCase: AddSessionUseCase,
        deleteSessionUseCase: DeleteSessionUseCase,
        getAllSessionsUseCase: GetAllSessionsUseCase,
        getRecentFiveSessionsUseCase: GetRecentFiveSessionsUseCase,
        getRecentTenSessionsForSubjectUseCase: GetRecentTenSessionsForSubjectUseCase,
        getTotalSessionsDurationUseCase: GetTotalSessionsDurationUseCase,
        getTotalSessionsDurationBySubjectUseCase: GetTotalSessionsDurationBySubjectUseCase
    ): SessionUseCases {
        return SessionUseCases(
            addSessionUseCase,
            deleteSessionUseCase,
            getAllSessionsUseCase,
            getRecentFiveSessionsUseCase,
            getRecentTenSessionsForSubjectUseCase,
            getTotalSessionsDurationUseCase,
            getTotalSessionsDurationBySubjectUseCase
        )
    }
}

