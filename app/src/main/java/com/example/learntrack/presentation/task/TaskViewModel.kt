package com.example.learntrack.presentation.task

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learntrack.domain.model.Task
import com.example.learntrack.domain.usecase.SessionUseCases
import com.example.learntrack.domain.usecase.SubjectUseCases
import com.example.learntrack.domain.usecase.TaskUseCases
import com.example.learntrack.presentation.navigation.TaskScreenNavigation
import com.example.learntrack.utils.SnackBarEvents
import com.example.learntrack.utils.SubjectPriority
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
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val subjectUseCase: SubjectUseCases,
    private val taskUseCases: TaskUseCases,
    private val sessionUseCases: SessionUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val navArgs = TaskScreenNavigation(
        subjectId = savedStateHandle["subjectId"] ?: 0,
        taskId = savedStateHandle["taskId"] ?: 0
    )

    private val _state = MutableStateFlow<TaskState>(TaskState())
    val state = combine(
        _state,
        subjectUseCase.getAllSubjects.invoke()
    ) { state, allSubjects ->
        state.copy(subjects = allSubjects)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = TaskState()
    )

    private val _snackBarEventFlow = MutableSharedFlow<SnackBarEvents>()
    val snackBarEventFlow = _snackBarEventFlow.asSharedFlow()


    init {
        fetchTask()
        fetchSubject()
    }

    fun onEvent(event: OnTaskEvent) {
        when (event) {
            is OnTaskEvent.OnDateChange -> {
                _state.update {
                    it.copy(dueDate = event.millis ?: 0L)
                }
            }

            is OnTaskEvent.OnDescriptionChange -> {
                _state.update {
                    it.copy(description = event.description)
                }
            }

            is OnTaskEvent.OnPriorityChange -> {
                _state.update {
                    it.copy(priority = event.priority)
                }
            }

            is OnTaskEvent.OnRelatedSubjectSelect -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.subject.name,
                        subjectId = event.subject.subjectId
                    )
                }
            }

            is OnTaskEvent.OnTitleChange -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            OnTaskEvent.OnIsCompleteChange -> {
                _state.update {
                    it.copy(
                        isCompleted = !_state.value.isCompleted
                    )
                }
            }

            OnTaskEvent.DeleteTask -> deleteTask()
            OnTaskEvent.SaveTask -> saveTask()
        }
    }

    private fun deleteTask() {
        viewModelScope.launch {
            try {
                val currentTaskId = state.value.currentTaskId
                if (currentTaskId != null) {
                    withContext(Dispatchers.IO) {
                        taskUseCases.deleteTaskUseCase(taskId = currentTaskId)
                    }
                    _snackBarEventFlow.emit(
                        SnackBarEvents.ShowSnackBar(message = "Task deleted successfully")
                    )
                    _snackBarEventFlow.emit(SnackBarEvents.NavigateUp)
                } else {
                    _snackBarEventFlow.emit(
                        SnackBarEvents.ShowSnackBar(message = "No Task to delete")
                    )
                }
            } catch (e: Exception) {
                _snackBarEventFlow.emit(
                    SnackBarEvents.ShowSnackBar(
                        message = "Couldn't delete task. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun saveTask() {
        viewModelScope.launch {
            val state = _state.value
            if (state.subjectId == null || state.relatedToSubject == null) {
                _snackBarEventFlow.emit(
                    SnackBarEvents.ShowSnackBar(
                        message = "Please select subject related to the task"
                    )
                )
                return@launch
            }
            try {
                taskUseCases.upsertTaskUseCase.invoke(
                    task = Task(
                        title = state.title,
                        description = state.description,
                        dueDate = state.dueDate ?: Instant.now().toEpochMilli(),
                        relatedToSubject = state.relatedToSubject,
                        priority = state.priority.value,
                        isComplete = state.isCompleted,
                        taskSubjectId = state.subjectId
                    )
                )
                _snackBarEventFlow.emit(
                    SnackBarEvents.ShowSnackBar(message = "Task Saved Successfully")
                )
                _snackBarEventFlow.emit(SnackBarEvents.NavigateUp)
            } catch (e: Exception) {
                _snackBarEventFlow.emit(
                    SnackBarEvents.ShowSnackBar(
                        message = "Couldn't save task. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun fetchTask() {
        viewModelScope.launch {
            navArgs.taskId?.let { id ->
                taskUseCases.getTaskByIdUseCase(id)?.let { task ->
                    _state.update {
                        it.copy(
                            title = task.title,
                            description = task.description,
                            dueDate = task.dueDate,
                            isCompleted = task.isComplete,
                            relatedToSubject = task.relatedToSubject,
                            priority = SubjectPriority.fromInt(task.priority),
                            subjectId = task.taskSubjectId,
                            currentTaskId = task.taskId
                        )
                    }
                }
            }
        }
    }

    private fun fetchSubject() {
        viewModelScope.launch {
            navArgs.subjectId?.let { id ->
                subjectUseCase.getSubjectById(id)?.let { subject ->
                    _state.update {
                        it.copy(
                            subjectId = subject.subjectId,
                            relatedToSubject = subject.name
                        )
                    }
                }
            }
        }
    }
}