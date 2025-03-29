package com.example.learntrack.presentation.session

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learntrack.domain.model.Session
import com.example.learntrack.domain.usecase.SessionUseCases
import com.example.learntrack.domain.usecase.SubjectUseCases
import com.example.learntrack.utils.SnackBarEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val subjectUseCases: SubjectUseCases,
    private val sessionUseCases: SessionUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(SessionState())
    val state = combine(
        _state,
        subjectUseCases.getAllSubjects.invoke(),
        sessionUseCases.getAllSessions.invoke(),
    ) { state, subject, session ->
        state.copy(
            subjects = subject,
            sessions = session
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = SessionState()
    )

    private val _snackbarEventFlow = MutableSharedFlow<SnackBarEvents>()
    val snackbarEventFlow = _snackbarEventFlow.asSharedFlow()

    fun onEvent(event: SessionEvent) {
        when (event) {
            SessionEvent.DeleteSession -> deleteSession()
            SessionEvent.NotifyToUpdateSubject -> notifyToUpdateSubject()

            is SessionEvent.OnDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(session = event.session)
                }
            }

            is SessionEvent.OnRelatedSubjectChange -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.subject.name,
                        subjectId = event.subject.subjectId
                    )
                }
            }

            is SessionEvent.SaveSession -> insertSession(event.duration)
            is SessionEvent.UpdateSubjectIdAndRelatedSubject -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.relatedToSubject,
                        subjectId = event.subjectId
                    )
                }
            }
        }
    }

    private fun notifyToUpdateSubject() {
        viewModelScope.launch {
            if (state.value.subjectId == null || state.value.relatedToSubject == null) {
                _snackbarEventFlow.emit(
                    SnackBarEvents.ShowSnackBar(
                        message = "Please select subject related to the session."
                    )
                )
            }
        }
    }

    private fun deleteSession() {
        viewModelScope.launch {
            try {
                state.value.session?.let {
                    sessionUseCases.deleteSession.invoke(it)
                    _snackbarEventFlow.emit(
                        SnackBarEvents.ShowSnackBar(message = "Session deleted successfully")
                    )
                }
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackBarEvents.ShowSnackBar(
                        message = "Couldn't delete session. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun insertSession(duration: Long) {
        viewModelScope.launch {
            if (duration < 36) {
                _snackbarEventFlow.emit(
                    SnackBarEvents.ShowSnackBar(
                        message = "Single session can not be less than 36 seconds"
                    )
                )
                return@launch
            }
            try {
                sessionUseCases.addSession.invoke(
                    session = Session(
                        sessionSubjectId = state.value.subjectId ?: -1,
                        relatedToSubject = state.value.relatedToSubject ?: "",
                        date = Instant.now().toEpochMilli(),
                        duration = duration
                    )
                )
                _snackbarEventFlow.emit(
                    SnackBarEvents.ShowSnackBar(message = "Session saved successfully")
                )
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackBarEvents.ShowSnackBar(
                        message = "Couldn't save session. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }
}