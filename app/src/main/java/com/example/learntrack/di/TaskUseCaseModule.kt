package com.example.learntrack.di

import com.example.learntrack.domain.repository.TaskRepository
import com.example.learntrack.domain.usecase.TaskUseCases
import com.example.learntrack.domain.usecase.task.DeleteTaskUseCase
import com.example.learntrack.domain.usecase.task.GetAllUpcomingTasksUseCase
import com.example.learntrack.domain.usecase.task.GetCompletedTasksForSubjectUseCase
import com.example.learntrack.domain.usecase.task.GetTaskByIdUseCase
import com.example.learntrack.domain.usecase.task.GetUpcomingTasksForSubjectUseCase
import com.example.learntrack.domain.usecase.task.UpsertTaskUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TaskUseCaseModule {

    @Provides
    fun provideUpsertTaskUseCase(repository: TaskRepository): UpsertTaskUseCase {
        return UpsertTaskUseCase(repository)
    }

    @Provides
    fun provideDeleteTaskUseCase(repository: TaskRepository): DeleteTaskUseCase {
        return DeleteTaskUseCase(repository)
    }

    @Provides
    fun provideGetTaskByIdUseCase(repository: TaskRepository): GetTaskByIdUseCase {
        return GetTaskByIdUseCase(repository)
    }

    @Provides
    fun provideGetAllUpcomingTasksUseCase(repository: TaskRepository): GetAllUpcomingTasksUseCase {
        return GetAllUpcomingTasksUseCase(repository)
    }

    @Provides
    fun provideGetUpcomingTaskForSubjectUseCase(repository: TaskRepository): GetUpcomingTasksForSubjectUseCase {
        return GetUpcomingTasksForSubjectUseCase(repository)
    }

    @Provides
    fun provideGetCompletedTasksForSubjectUseCase(repository: TaskRepository): GetCompletedTasksForSubjectUseCase {
        return GetCompletedTasksForSubjectUseCase(repository)
    }

    @Provides
    fun provideTaskUseCase(
        deleteTaskUseCase: DeleteTaskUseCase,
        getAllUpcomingTasksUseCase: GetAllUpcomingTasksUseCase,
        getCompletedTasksForSubjectUseCase: GetCompletedTasksForSubjectUseCase,
        getTaskByIdUseCase: GetTaskByIdUseCase,
        getUpcomingTasksForSubjectUseCase: GetUpcomingTasksForSubjectUseCase,
        upsertTaskUseCase: UpsertTaskUseCase,
    ): TaskUseCases {
        return TaskUseCases(
            deleteTaskUseCase,
            getAllUpcomingTasksUseCase,
            getCompletedTasksForSubjectUseCase,
            getTaskByIdUseCase,
            getUpcomingTasksForSubjectUseCase,
            upsertTaskUseCase
        )
    }
}