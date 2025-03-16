package com.example.learntrack.presentation.subject

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learntrack.domain.model.Subject
import com.example.learntrack.domain.model.Task
import com.example.learntrack.domain.usecase.SessionUseCases
import com.example.learntrack.domain.usecase.SubjectUseCases
import com.example.learntrack.domain.usecase.TaskUseCases
import com.example.learntrack.presentation.navigation.SubjectScreenNavigation
import com.example.learntrack.utils.SnackBarEvents
import com.example.learntrack.utils.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    sessionUseCase: SessionUseCases,
    private val subjectUseCase: SubjectUseCases,
    private val taskUseCase: TaskUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val navArgs = SubjectScreenNavigation(
        subjectId = savedStateHandle["subjectId"] ?: 0
    )

    init {
        fetchSubject()
    }

    private val _snackbarEventFlow = MutableSharedFlow<SnackBarEvents>()
    val snackbarEventFlow = _snackbarEventFlow.asSharedFlow()

    private val _state = MutableStateFlow(SubjectState())
    val state = combine(
        _state,
        taskUseCase.getUpcomingTasksForSubjectUseCase.invoke(navArgs.subjectId),
        taskUseCase.getCompletedTasksForSubjectUseCase.invoke(navArgs.subjectId),
        sessionUseCase.getRecentTenSessionsForSubject.invoke(navArgs.subjectId),
        sessionUseCase.getTotalSessionsDurationBySubject.invoke(navArgs.subjectId)
    ) { state, upcomingTasks, completedTasks, recentSessions, totalSessionsDuration ->
        state.copy(
            upcomingTasks = upcomingTasks,
            completedTasks = completedTasks,
            recentSessions = recentSessions,
            studiedHours = totalSessionsDuration.toHours()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = SubjectState()
    )

    fun onEvent(event: OnSubjectEvent) {
        viewModelScope.launch {
            when (event) {
                is OnSubjectEvent.OnDeleteSessionButtonClick -> {
                    _state.update {
                        it.copy(session = event.session)
                    }
                }

                is OnSubjectEvent.OnGoalStudyHoursChange -> {
                    _state.update {
                        it.copy(goalStudyHours = event.hours)
                    }
                }

                is OnSubjectEvent.OnSubjectCardColorChange -> {
                    _state.update {
                        it.copy(subjectCardColors = event.color)
                    }
                }

                is OnSubjectEvent.OnSubjectNameChange -> {
                    _state.update {
                        it.copy(subjectName = event.name)
                    }
                }

                is OnSubjectEvent.OnTaskIsCompleteChange -> {
                    updateTask(event.task)
                }

                OnSubjectEvent.UpdateProgress -> {
                    val goalStudyHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f

                    _state.update {
                        it.copy(
                            progress = (state.value.studiedHours / goalStudyHours).coerceIn(0f, 1f)
                        )
                    }
                }

                OnSubjectEvent.DeleteSession -> TODO()

                OnSubjectEvent.DeleteSubject -> deleteSubject()

                OnSubjectEvent.UpdateSubject -> {
                    updateSubject()
                }
            }
        }
    }

    private fun fetchSubject() {
        viewModelScope.launch {
            subjectUseCase.getSubjectById.invoke(navArgs.subjectId)?.let { subject ->
                _state.update {
                    it.copy(
                        subjectName = subject.name,
                        currentSubjectId = subject.subjectId,
                        goalStudyHours = subject.goalHours.toString(),
                        subjectCardColors = subject.colors.map { colors -> Color(colors) }
                    )
                }
            }
        }
    }

    private fun updateSubject() {
        viewModelScope.launch {
            try {
                subjectUseCase.upsertSubject.invoke(
                    subject = Subject(
                        subjectId = state.value.currentSubjectId,
                        name = state.value.subjectName,
                        goalHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                        colors = state.value.subjectCardColors.map { it.toArgb() }
                    )
                )
                _snackbarEventFlow.emit(
                    SnackBarEvents.ShowSnackBar(message = "Subject updated successfully.")
                )
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackBarEvents.ShowSnackBar(
                        message = "Couldn't update subject. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun deleteSubject() {
        viewModelScope.launch {
            try {
                val subjectId = state.value.currentSubjectId
                if (subjectId != null) {
                    withContext(Dispatchers.IO) {
                        subjectUseCase.deleteSubject.invoke(subjectId)
                    }

                    _snackbarEventFlow.emit(
                        SnackBarEvents.ShowSnackBar(message = "Subject deleted successfully."),
                    )
                    _snackbarEventFlow.emit(
                        SnackBarEvents.NavigateUp
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "deleteSubject: ${e.localizedMessage}")
                SnackBarEvents.ShowSnackBar(
                    message = "Subject something went wrong. ${e.message}",
                    duration = SnackbarDuration.Long
                )
            }

        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                taskUseCase.upsertTaskUseCase(
                    task = task.copy(isComplete = !task.isComplete)
                )
                if (task.isComplete) {
                    _snackbarEventFlow.emit(
                        SnackBarEvents.ShowSnackBar(message = "Saved in upcoming tasks.")
                    )
                } else {
                    _snackbarEventFlow.emit(
                        SnackBarEvents.ShowSnackBar(message = "Saved in completed tasks.")
                    )
                }
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackBarEvents.ShowSnackBar(
                        message = "Couldn't update task. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    companion object {
        const val TAG = "SubjectViewModel"
    }
}