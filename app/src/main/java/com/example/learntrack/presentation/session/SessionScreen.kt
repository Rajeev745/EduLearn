package com.example.learntrack.presentation.session

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.learntrack.presentation.components.DeleteDialog
import com.example.learntrack.presentation.components.SubjectListBottomSheet
import com.example.learntrack.presentation.components.studySessionList
import com.example.learntrack.presentation.session.StudySessionServiceHelper.triggerForegroundService
import com.example.learntrack.presentation.theme.Red
import com.example.learntrack.utils.SnackBarEvents
import com.example.learntrack.utils.StudyConstants.ServiceConstants.ACTION_SERVICE_CANCEL
import com.example.learntrack.utils.StudyConstants.ServiceConstants.ACTION_SERVICE_START
import com.example.learntrack.utils.StudyConstants.ServiceConstants.ACTION_SERVICE_STOP
import com.example.learntrack.utils.StudyConstants.UtilityConstant.EMPTY_STRING
import com.example.learntrack.utils.timerTextAnimation
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.DurationUnit

@Composable
fun SessionScreenRoute(navController: NavHostController, timerServices: StudySessionTimerService) {

    val sessionViewModel: SessionViewModel = hiltViewModel()
    val state by sessionViewModel.state.collectAsStateWithLifecycle()
    val snackbarEvent = sessionViewModel.snackbarEventFlow
    val onEvent = sessionViewModel::onEvent

    SessionScreen(
        state,
        snackbarEvent,
        onEvent,
        onBackButtonClicked = { navController.popBackStack() },
        timerService = timerServices
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
    state: SessionState,
    snackbarEvent: SharedFlow<SnackBarEvents>,
    onEvent: (SessionEvent) -> Unit,
    onBackButtonClicked: () -> Unit,
    timerService: StudySessionTimerService,
) {

    val seconds by timerService.seconds
    val minutes by timerService.minutes
    val hours by timerService.hours
    val currentTimerState by timerService.currentTimerState

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by remember { mutableStateOf(false) }

    var deleteSessionDialogOpened by rememberSaveable {
        mutableStateOf(false)
    }


    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        snackbarEvent.collectLatest { event ->
            when (event) {
                is SnackBarEvents.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                SnackBarEvents.NavigateUp -> {}
            }
        }
    }

    LaunchedEffect(key1 = state.subjects) {
        val subjectId = timerService.subjectId.value
        onEvent(
            SessionEvent.UpdateSubjectIdAndRelatedSubject(
                subjectId = subjectId,
                relatedToSubject = state.subjects.find { it.subjectId == subjectId }?.name
            )
        )
    }

    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isBottomSheetOpen,
        subjects = state.subjects,
        onDismissRequest = { isBottomSheetOpen = false },
        onSubjectClicked = { subject ->
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) isBottomSheetOpen = false
            }
            onEvent(SessionEvent.OnRelatedSubjectChange(subject))
        }
    )

    DeleteDialog(
        isOpen = deleteSessionDialogOpened,
        title = "Delete session",
        description = "Are you sure you want to delete this session?",
        onDismissRequestClick = { deleteSessionDialogOpened = false },
        onConfirmButtonClick = {
            onEvent(SessionEvent.DeleteSession)
            deleteSessionDialogOpened = false
        }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            SessionScreenTopBar(onBackButtonClicked = onBackButtonClicked)
        }) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                TimerSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds
                )
            }

            item {
                RelatedToSubject(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    relatedToSubject = state.relatedToSubject ?: EMPTY_STRING,
                    selectSubjectButtonClick = { isBottomSheetOpen = true },
                    seconds = seconds
                )
            }

            item {
                SessionButtons(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onStartButtonClick = {

                        if (state.subjectId != null && state.relatedToSubject != null) {
                            triggerForegroundService(
                                context = context,
                                action = if (currentTimerState == TimerState.STARTED) ACTION_SERVICE_STOP
                                else ACTION_SERVICE_START
                            )
                            timerService.subjectId.value = state.subjectId
                        } else {
                            onEvent(SessionEvent.NotifyToUpdateSubject)
                        }
                    },
                    onFinishButtonClick = {
                        val duration = timerService.duration.toLong(DurationUnit.SECONDS)
                        if (duration >= 36) {
                            triggerForegroundService(
                                context = context,
                                action = ACTION_SERVICE_CANCEL
                            )
                        }
                        onEvent(SessionEvent.SaveSession(duration))
                    },
                    onCancelButtonClick = {
                        triggerForegroundService(
                            context = context, action = ACTION_SERVICE_CANCEL
                        )
                    },
                    timerState = currentTimerState,
                    seconds = seconds
                )
            }

            studySessionList(
                sectionTitle = "STUDY SESSION HISTORY",
                sessions = state.sessions,
                emptyListText = "You don't have any recent study history",
                onDeleteSessionClick = { session ->
                    deleteSessionDialogOpened = true
                    onEvent(SessionEvent.OnDeleteSessionButtonClick(session))
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreenTopBar(
    onBackButtonClicked: () -> Unit,
) {

    TopAppBar(title = {
        Text(text = "Study Session")
    }, navigationIcon = {
        IconButton(onClick = onBackButtonClicked) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "Navigate to Back"
            )
        }
    })

}

@Composable
private fun TimerSection(
    modifier: Modifier = Modifier,
    seconds: String,
    minutes: String,
    hours: String,
) {

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .border(5.dp, color = MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
        )

        Row {
            AnimatedContent(
                targetState = hours,
                label = hours,
                transitionSpec = { timerTextAnimation() }) { hours ->
                Text(
                    text = "$hours:",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                )
            }

            AnimatedContent(
                targetState = minutes,
                label = minutes,
                transitionSpec = { timerTextAnimation() }) { minutes ->
                Text(
                    text = "$minutes:",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                )
            }

            AnimatedContent(
                targetState = seconds,
                label = seconds,
                transitionSpec = { timerTextAnimation() }) { seconds ->
                Text(
                    text = seconds,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                )
            }
        }
    }

}

@Composable
private fun RelatedToSubject(
    modifier: Modifier,
    relatedToSubject: String,
    selectSubjectButtonClick: () -> Unit,
    seconds: String,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "Related to subject", style = MaterialTheme.typography.bodyMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = relatedToSubject, style = MaterialTheme.typography.bodyLarge)

            IconButton(onClick = selectSubjectButtonClick, enabled = seconds == "00") {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown, contentDescription = "Select Subject"
                )
            }
        }
    }
}

@Composable
private fun SessionButtons(
    modifier: Modifier,
    onStartButtonClick: () -> Unit,
    onFinishButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit,
    timerState: TimerState,
    seconds: String,
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Button(
            onClick = onCancelButtonClick,
            enabled = seconds != "00" && timerState == TimerState.STARTED
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Cancel",
                style = MaterialTheme.typography.labelMedium
            )
        }

        Button(
            onClick = onStartButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (timerState == TimerState.STARTED) Red
                else MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = when (timerState) {
                    TimerState.STARTED -> "Stop"
                    TimerState.STOPPED -> "Resume"
                    else -> "Start"
                },
                style = MaterialTheme.typography.labelMedium
            )
        }

        Button(
            onClick = onFinishButtonClick,
            enabled = seconds != "00" && timerState != TimerState.STARTED
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Stop",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}