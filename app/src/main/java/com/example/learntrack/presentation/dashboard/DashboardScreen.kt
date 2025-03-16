package com.example.learntrack.presentation.dashboard

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.learntrack.R
import com.example.learntrack.domain.model.Session
import com.example.learntrack.domain.model.Subject
import com.example.learntrack.domain.model.Task
import com.example.learntrack.presentation.components.AddSubjectDialog
import com.example.learntrack.presentation.components.CountCard
import com.example.learntrack.presentation.components.DeleteDialog
import com.example.learntrack.presentation.components.SubjectCard
import com.example.learntrack.presentation.components.studySessionList
import com.example.learntrack.presentation.components.taskList
import com.example.learntrack.presentation.navigation.SessionScreenNavigation
import com.example.learntrack.presentation.navigation.SubjectScreenNavigation
import com.example.learntrack.presentation.navigation.TaskScreenNavigation
import com.example.learntrack.utils.SnackBarEvents
import com.example.learntrack.utils.StudyConstants.TAGS.DASHBOARD_TAG
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DashboardScreenRoute(navController: NavHostController) {

    val dashBoardViewModel: DashboardViewModel = hiltViewModel()
    val state by dashBoardViewModel.state.collectAsStateWithLifecycle()
    val tasks by dashBoardViewModel.tasks.collectAsStateWithLifecycle()
    val sessions by dashBoardViewModel.sessionsList.collectAsStateWithLifecycle()

    val event = dashBoardViewModel::onEvent

    DashboardScreen(
        state = state,
        event = event,
        snackBarEvent = dashBoardViewModel.snackBarEventFlow,
        tasks = tasks,
        sessions = sessions,
        onSessionClick = {
            navController.navigate(SessionScreenNavigation)
        },
        onTaskClick = { subjectId, taskId ->
            navController.navigate(
                TaskScreenNavigation(
                    subjectId = subjectId,
                    taskId = taskId
                )
            )
        },
        onSubjectClick = {
            navController.navigate(SubjectScreenNavigation(it))
        }
    )
}

@Composable
fun DashboardScreen(
    state: DashboardState,
    event: (DashboardEvent) -> Unit,
    snackBarEvent: SharedFlow<SnackBarEvents>,
    onSessionClick: () -> Unit,
    onTaskClick: (subjectId: Int, taskId: Int) -> Unit,
    onSubjectClick: (subjectId: Int) -> Unit,
    tasks: List<Task>,
    sessions: List<Session>,
) {

    var isAddSubjectDialogOpened by rememberSaveable {
        mutableStateOf(false)
    }

    var deleteSubjectDialogOpened by rememberSaveable {
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

                else -> Unit
            }
        }
    }

    DeleteDialog(
        isOpen = deleteSubjectDialogOpened,
        title = "Delete session",
        description = "Are you sure you want to delete this session?",
        onDismissRequestClick = { deleteSubjectDialogOpened = false },
        onConfirmButtonClick = {
            event(DashboardEvent.DeleteSession)
            deleteSubjectDialogOpened = false
        }
    )

    AddSubjectDialog(
        isOpen = isAddSubjectDialogOpened,
        title = "Add/Update Subject",
        subjectName = state.subjectName,
        subjectGoalHour = state.goalStudyHours,
        onSubjectNameChange = { event(DashboardEvent.OnSubjectNameChange(it)) },
        onSubjectGoalHourChange = { event(DashboardEvent.OnGoalStudyHoursChange(it)) },
        selectedColorList = state.subjectCardColors,
        onColorChange = {
            Log.d(DASHBOARD_TAG, "DashboardScreen: $it")
            event(DashboardEvent.OnSubjectCardColorChange(it))
        },
        onDismissRequestClick = { isAddSubjectDialogOpened = false },
        onConfirmButtonClick = {
            event(DashboardEvent.SaveSubject)
            isAddSubjectDialogOpened = false
        }
    )

    // Scaffold helps us in putting elements like top app bar, floating button and bottom nav bar
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = { DashboardScreenTopBar() }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                CountCardSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    subjectCount = state.totalSubjectCount,
                    studiedHours = state.totalStudiedHours.toString(),
                    goalStudyHours = state.goalStudyHours
                )
            }
            item {
                SubjectCardSection(
                    subjectList = state.subjects,
                    modifier = Modifier.fillMaxWidth(),
                    onAddSubjectClicked = {
                        isAddSubjectDialogOpened = true
                    }, onSubjectClick = { subjectId ->
                        onSubjectClick(subjectId)
                        Log.d(DASHBOARD_TAG, "DashboardScreen: $subjectId")
                    })
            }
            item {
                Button(
                    onClick = onSessionClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp, vertical = 20.dp)
                ) {
                    Text(text = "Start study session")
                }
            }
            taskList(
                sectionTitle = "UPCOMING TASKS",
                tasks = tasks,
                emptyListText = "You don't have any upcoming tasks as of now. \n please click on + button to add new tasks",
                onCheckBoxClick = { event(DashboardEvent.OnTaskIsCompleteChange(it)) },
                onTaskCardClick = { taskId ->
                    onTaskClick(-1, taskId ?: -1)
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
                sectionTitle = "RECENT STUDY SESSION",
                sessions = sessions,
                emptyListText = "You don't have any recent study history",
                onDeleteSessionClick = {
                    event(DashboardEvent.OnDeleteSessionButtonClick(it))
                    deleteSubjectDialogOpened = true
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenTopBar() {
    CenterAlignedTopAppBar(title = {
        Text(text = "Lean Track", style = MaterialTheme.typography.headlineLarge)
    })
}

@Composable
private fun CountCardSection(
    modifier: Modifier = Modifier,
    subjectCount: Int,
    studiedHours: String,
    goalStudyHours: String,
) {

    Row(modifier = modifier) {
        CountCard(
            modifier = Modifier.weight(1f),
            heading = "Subject Count",
            count = subjectCount.toString()
        )
        Spacer(modifier = Modifier.width(16.dp))
        CountCard(
            modifier = Modifier.weight(1f), heading = "Studied Hours", count = studiedHours
        )
        Spacer(modifier = Modifier.width(16.dp))
        CountCard(
            modifier = Modifier.weight(1f), heading = "Goal Study Hours", count = goalStudyHours
        )
    }
}

@Composable
private fun SubjectCardSection(
    modifier: Modifier = Modifier,
    subjectList: List<Subject>,
    emptyListText: String = "You have not added any Subjects. \n Please Click on the + button to add the new Subjects",
    onAddSubjectClicked: () -> Unit,
    onSubjectClick: (Int) -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "SUBJECTS",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp)
            )

            IconButton(onClick = { onAddSubjectClicked() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Subject")
            }

        }

        if (subjectList.isEmpty()) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.ic_book_three),
                contentDescription = emptyListText
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            items(subjectList) { subject ->
                SubjectCard(
                    subjectName = subject.name,
                    gradientColors = subject.colors.map { Color(it) },
                    onClick = { onSubjectClick(subject.subjectId ?: 0) }
                )
            }
        }
    }
}