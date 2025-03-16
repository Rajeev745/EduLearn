package com.example.learntrack.presentation.dashboard

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learntrack.domain.model.Session
import com.example.learntrack.domain.model.Subject
import com.example.learntrack.domain.model.Task
import com.example.learntrack.domain.usecase.SessionUseCases
import com.example.learntrack.domain.usecase.SubjectUseCases
import com.example.learntrack.domain.usecase.TaskUseCases
import com.example.learntrack.utils.SnackBarEvents
import com.example.learntrack.utils.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    sessionUseCase: SessionUseCases,
    private val subjectUseCase: SubjectUseCases,
    private val taskUseCase: TaskUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = combine(
        _state,
        subjectUseCase.getTotalSubjectCount.invoke(),
        subjectUseCase.getTotalGoalHours.invoke(),
        subjectUseCase.getAllSubjects.invoke(),
        sessionUseCase.getTotalSessionsDuration.invoke()
    ) { _state, subjectCount, goalHours, subjects, totalSessionDuration ->
        _state.copy(
            totalSubjectCount = subjectCount,
            totalGoalStudyHours = goalHours,
            subjects = subjects,
            totalStudiedHours = totalSessionDuration.toHours()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardState()
    )

    val tasks: StateFlow<List<Task>> = taskUseCase.getAllUpcomingTasksUseCase.invoke()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val sessionsList: StateFlow<List<Session>> = sessionUseCase.getAllSessions.invoke()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _snackBarEventFlow = MutableSharedFlow<SnackBarEvents>()
    val snackBarEventFlow = _snackBarEventFlow.asSharedFlow()

    fun onEvent(event: DashboardEvent) {
        when (event) {
            DashboardEvent.DeleteSession -> TODO()
            is DashboardEvent.OnDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(
                        session = event.session
                    )
                }
            }

            is DashboardEvent.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy(
                        goalStudyHours = event.hours
                    )
                }
            }

            is DashboardEvent.OnSubjectCardColorChange -> {
                _state.update {
                    it.copy(
                        subjectCardColors = event.colors
                    )
                }
            }

            is DashboardEvent.OnSubjectNameChange -> {
                _state.update {
                    it.copy(
                        subjectName = event.name
                    )
                }
            }

            is DashboardEvent.OnTaskIsCompleteChange -> {
                updateTask(event.task)
            }

            DashboardEvent.SaveSubject -> {
                saveSubject()
            }
        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                taskUseCase.upsertTaskUseCase(
                    task = task.copy(isComplete = !task.isComplete)
                )
                _snackBarEventFlow.emit(
                    SnackBarEvents.ShowSnackBar(message = "Saved in completed tasks.")
                )
            } catch (e: Exception) {
                _snackBarEventFlow.emit(
                    SnackBarEvents.ShowSnackBar(
                        "Couldn't update task. ${e.message}",
                        SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun saveSubject() {
        viewModelScope.launch {
            try {

                subjectUseCase.upsertSubject.invoke(
                    Subject(
                        name = _state.value.subjectName,
                        goalHours = _state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                        colors = _state.value.subjectCardColors.map { it.toArgb() }
                    )
                )

                _state.update {
                    it.copy(
                        subjectName = "",
                        goalStudyHours = "",
                        subjectCardColors = Subject.subjectCardColors.random()
                    )
                }

                _snackBarEventFlow.emit(
                    SnackBarEvents.ShowSnackBar(
                        message = "Subject saved successfully",
                        duration = SnackbarDuration.Long
                    )
                )
            } catch (e: Exception) {
                Log.d(TAG, "saveSubject: ${e.localizedMessage}")
                _snackBarEventFlow.emit(
                    SnackBarEvents.ShowSnackBar(
                        message = "Something went wrong ${e.localizedMessage}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    companion object {
        const val TAG = "DashboardViewModel"
    }
}