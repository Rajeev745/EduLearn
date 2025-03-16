package com.example.learntrack.domain.usecase

import com.example.learntrack.domain.usecase.session.AddSessionUseCase
import com.example.learntrack.domain.usecase.session.DeleteSessionUseCase
import com.example.learntrack.domain.usecase.session.GetAllSessionsUseCase
import com.example.learntrack.domain.usecase.session.GetRecentFiveSessionsUseCase
import com.example.learntrack.domain.usecase.session.GetRecentTenSessionsForSubjectUseCase
import com.example.learntrack.domain.usecase.session.GetTotalSessionsDurationBySubjectUseCase
import com.example.learntrack.domain.usecase.session.GetTotalSessionsDurationUseCase
import com.example.learntrack.domain.usecase.subject.DeleteSubjectUseCase
import com.example.learntrack.domain.usecase.subject.GetAllSubjectsUseCase
import com.example.learntrack.domain.usecase.subject.GetSubjectByIdUseCase
import com.example.learntrack.domain.usecase.subject.GetTotalGoalHoursUseCase
import com.example.learntrack.domain.usecase.subject.GetTotalSubjectCountUseCase
import com.example.learntrack.domain.usecase.subject.UpsertSubjectUseCase
import com.example.learntrack.domain.usecase.task.DeleteTaskUseCase
import com.example.learntrack.domain.usecase.task.GetAllUpcomingTasksUseCase
import com.example.learntrack.domain.usecase.task.GetCompletedTasksForSubjectUseCase
import com.example.learntrack.domain.usecase.task.GetTaskByIdUseCase
import com.example.learntrack.domain.usecase.task.GetUpcomingTasksForSubjectUseCase
import com.example.learntrack.domain.usecase.task.UpsertTaskUseCase
import javax.inject.Inject

class SessionUseCases @Inject constructor(
    val addSession: AddSessionUseCase,
    val deleteSession: DeleteSessionUseCase,
    val getAllSessions: GetAllSessionsUseCase,
    val getRecentFiveSessions: GetRecentFiveSessionsUseCase,
    val getRecentTenSessionsForSubject: GetRecentTenSessionsForSubjectUseCase,
    val getTotalSessionsDuration: GetTotalSessionsDurationUseCase,
    val getTotalSessionsDurationBySubject: GetTotalSessionsDurationBySubjectUseCase,
)

class SubjectUseCases @Inject constructor(
    val deleteSubject: DeleteSubjectUseCase,
    val getAllSubjects: GetAllSubjectsUseCase,
    val getSubjectById: GetSubjectByIdUseCase,
    val upsertSubject: UpsertSubjectUseCase,
    val getTotalGoalHours: GetTotalGoalHoursUseCase,
    val getTotalSubjectCount: GetTotalSubjectCountUseCase,
)

class TaskUseCases @Inject constructor(
    val deleteTaskUseCase: DeleteTaskUseCase,
    val getAllUpcomingTasksUseCase: GetAllUpcomingTasksUseCase,
    val getCompletedTasksForSubjectUseCase: GetCompletedTasksForSubjectUseCase,
    val getTaskByIdUseCase: GetTaskByIdUseCase,
    val getUpcomingTasksForSubjectUseCase: GetUpcomingTasksForSubjectUseCase,
    val upsertTaskUseCase: UpsertTaskUseCase
)
