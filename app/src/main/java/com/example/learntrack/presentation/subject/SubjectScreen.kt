package com.example.learntrack.presentation.subject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.learntrack.presentation.components.AddSubjectDialog
import com.example.learntrack.presentation.components.CountCard
import com.example.learntrack.presentation.components.DeleteDialog
import com.example.learntrack.presentation.components.studySessionList
import com.example.learntrack.presentation.components.taskList
import com.example.learntrack.presentation.navigation.SubjectScreenNavigation
import com.example.learntrack.presentation.navigation.TaskScreenNavigation
import com.example.learntrack.utils.SnackBarEvents
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SubjectScreenRoute(navController: NavHostController, args: SubjectScreenNavigation) {

    val subjectViewModel: SubjectViewModel = hiltViewModel()
    val state by subjectViewModel.state.collectAsStateWithLifecycle()

    SubjectScreen(
        state = state,
        onEvent = subjectViewModel::onEvent,
        snackBarEvent = subjectViewModel.snackbarEventFlow,
        onBackButtonClick = { navController.popBackStack() },
        onTaskClick = { subjectId, taskId ->
            navController.navigate(TaskScreenNavigation(subjectId = subjectId, taskId = taskId))
        },
        onAddTaskButtonClick = {
            navController.navigate(TaskScreenNavigation(subjectId = state.currentSubjectId ?: 0, taskId = 0))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreen(
    state: SubjectState,
    onEvent: (OnSubjectEvent) -> Unit,
    snackBarEvent: SharedFlow<SnackBarEvents>,
    onBackButtonClick: () -> Unit,
    onTaskClick: (subjectId: Int, taskId: Int) -> Unit,
    onAddTaskButtonClick: () -> Unit
) {

    val listState = rememberLazyListState()
    val isFloatingButtonExtended by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }

    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var isEditSubjectDialogOpened by rememberSaveable {
        mutableStateOf(false)
    }

    var deleteSubjectDialogOpened by rememberSaveable {
        mutableStateOf(false)
    }

    var deleteSessionDialogOpened by rememberSaveable {
        mutableStateOf(false)
    }

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        snackBarEvent.collectLatest { event ->
            when (event) {
                is SnackBarEvents.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                SnackBarEvents.NavigateUp -> {
                    onBackButtonClick()
                }
            }
        }
    }

    LaunchedEffect(key1 = state.goalStudyHours, key2 = state.studiedHours) {
        onEvent(OnSubjectEvent.UpdateProgress)
    }

    DeleteDialog(
        isOpen = deleteSessionDialogOpened,
        title = "Delete session",
        description = "Are you sure you want to delete this session?",
        onDismissRequestClick = { deleteSessionDialogOpened = false },
        onConfirmButtonClick = {
            onEvent(OnSubjectEvent.DeleteSession)
            deleteSessionDialogOpened = false
        }
    )

    DeleteDialog(
        isOpen = deleteSubjectDialogOpened,
        title = "Delete session",
        description = "Are you sure you want to delete this subject?",
        onDismissRequestClick = { deleteSubjectDialogOpened = false },
        onConfirmButtonClick = {
            onEvent(OnSubjectEvent.DeleteSubject)
            deleteSubjectDialogOpened = false
        }
    )

    AddSubjectDialog(
        isOpen = isEditSubjectDialogOpened,
        title = "Add/Update Subject",
        subjectName = state.subjectName,
        subjectGoalHour = state.goalStudyHours,
        onSubjectNameChange = { onEvent(OnSubjectEvent.OnSubjectNameChange(it)) },
        onSubjectGoalHourChange = { onEvent(OnSubjectEvent.OnGoalStudyHoursChange(it)) },
        selectedColorList = state.subjectCardColors,
        onColorChange = { onEvent(OnSubjectEvent.OnSubjectCardColorChange(it)) },
        onDismissRequestClick = { isEditSubjectDialogOpened = false },
        onConfirmButtonClick = {
            onEvent(OnSubjectEvent.UpdateSubject)
            isEditSubjectDialogOpened = false
        }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            SubjectScreenTopBar(
                title = state.subjectName,
                onBackButtonClick = onBackButtonClick,
                onDeleteSubjectClick = { deleteSubjectDialogOpened = true },
                onEditButtonClick = { isEditSubjectDialogOpened = true },
                scrollBehaviour = scrollBehaviour
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTaskButtonClick,
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add") },
                text = { Text(text = "Add") },
                expanded = isFloatingButtonExtended
            )
        })
    { paddingValues ->

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            item {
                SubjectOverviewSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    studiedHours = state.studiedHours.toString(),
                    goalHours = state.goalStudyHours.toString(),
                    progress = state.progress
                )
            }

            taskList(
                sectionTitle = "UPCOMING TASKS",
                tasks = state.upcomingTasks,
                emptyListText = "You don't have any upcoming tasks as of now.",
                onCheckBoxClick = { onEvent(OnSubjectEvent.OnTaskIsCompleteChange(it)) },
                onTaskCardClick = {
                    onTaskClick(-1, it ?: -1)
                }
            )

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
            }

            taskList(
                sectionTitle = "COMPLETED TASKS",
                tasks = state.completedTasks,
                emptyListText = "You don't have any completed tasks as of now. \n please mark the tasks as completed",
                onCheckBoxClick = { onEvent(OnSubjectEvent.OnTaskIsCompleteChange(it)) },
                onTaskCardClick = {
                    onTaskClick(-1, it ?: -1)
                }
            )

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
            }

            studySessionList(
                sectionTitle = "COMPLETED STUDY SESSION",
                sessions = state.recentSessions,
                emptyListText = "You don't have any recent study history",
                onDeleteSessionClick = {
                    onEvent(OnSubjectEvent.OnDeleteSessionButtonClick(it))
                    deleteSessionDialogOpened = true
                }
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectScreenTopBar(
    title: String,
    onBackButtonClick: () -> Unit,
    onDeleteSubjectClick: () -> Unit,
    onEditButtonClick: () -> Unit,
    scrollBehaviour: TopAppBarScrollBehavior,
) {
    LargeTopAppBar(
        scrollBehavior = scrollBehaviour,
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
        }, navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Navigate Back")
            }
        }, actions = {
            IconButton(onClick = onDeleteSubjectClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Subject")
            }
            IconButton(onClick = onEditButtonClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Subject")
            }
        })
}

@Composable
private fun SubjectOverviewSection(
    modifier: Modifier,
    studiedHours: String,
    goalHours: String,
    progress: Float,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        val percentageProgress = rememberSaveable(progress) {
            (progress * 100).toInt().coerceIn(0, 100)
        }

        CountCard(modifier = Modifier.weight(1f), heading = "Goal Study Hours", count = goalHours)

        Spacer(modifier = Modifier.width(10.dp))

        CountCard(modifier = Modifier.weight(1f), heading = "Studied Hours", count = studiedHours)

        Spacer(modifier = Modifier.width(10.dp))

        Box(modifier = Modifier.size(75.dp), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = 1f,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = progress,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
            )
            Text(text = "$percentageProgress%")
        }

    }
}