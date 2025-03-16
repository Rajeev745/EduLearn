package com.example.learntrack.presentation.task

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.learntrack.presentation.components.DeleteDialog
import com.example.learntrack.presentation.components.SubjectListBottomSheet
import com.example.learntrack.presentation.components.TaskCheckBox
import com.example.learntrack.presentation.components.TaskDatePicker
import com.example.learntrack.utils.SnackBarEvents
import com.example.learntrack.utils.StudyConstants
import com.example.learntrack.utils.StudyConstants.TAGS.TASK_TAG
import com.example.learntrack.utils.SubjectPriority
import com.example.learntrack.utils.changeMillisToDateString
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun TaskScreenRoute(navController: NavHostController) {

    val taskViewModel: TaskViewModel = hiltViewModel()
    val state by taskViewModel.state.collectAsStateWithLifecycle()
    TaskScreen(
        state = state,
        onEvent = taskViewModel::onEvent,
        snackBarEvent = taskViewModel.snackBarEventFlow,
        onBackButtonClick = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    state: TaskState,
    onEvent: (OnTaskEvent) -> Unit,
    snackBarEvent: SharedFlow<SnackBarEvents>,
    onBackButtonClick: () -> Unit,
) {

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

                is SnackBarEvents.NavigateUp -> {
                    onBackButtonClick()
                }
            }
        }
    }

    var titleError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var deleteTaskDialogOpened by rememberSaveable {
        mutableStateOf(false)
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )

    var datePickerDialogOpened by rememberSaveable {
        mutableStateOf(false)
    }

    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by remember {
        mutableStateOf(false)
    }
    val bottomSheetScope = rememberCoroutineScope()

    titleError = when {
        state.title.isBlank() -> "Please enter Title."
        state.title.length < 4 -> "Subject name is too short."
        state.title.length > 30 -> "Subject name is too long."
        else -> null
    }

    DeleteDialog(
        isOpen = deleteTaskDialogOpened,
        title = "Delete task",
        description = "Are you sure you want to delete this task?",
        onDismissRequestClick = { deleteTaskDialogOpened = false },
        onConfirmButtonClick = {
            onEvent(OnTaskEvent.DeleteTask)
            deleteTaskDialogOpened = false
        }
    )

    TaskDatePicker(
        state = datePickerState,
        isOpen = datePickerDialogOpened,
        onDismissRequestClicked = { datePickerDialogOpened = false },
        onConfirmButtonClicked = {
            onEvent(OnTaskEvent.OnDateChange(datePickerState.selectedDateMillis))
            datePickerDialogOpened = false
        }
    )

    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isBottomSheetOpen,
        subjects = state.subjects,
        onSubjectClicked = {
            bottomSheetScope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) isBottomSheetOpen = false
            }
            onEvent(OnTaskEvent.OnRelatedSubjectSelect(it))
            Log.d(TASK_TAG, "TaskScreen: $it")
        },
        onDismissRequest = { isBottomSheetOpen = false }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TaskScreenTopBar(
                isTaskExists = state.currentTaskId != 0,
                isCompleted = state.isCompleted,
                checkBoxBorderColor = Color.Green,
                onBackButtonClick = onBackButtonClick,
                onDeleteButtonClick = {
                    deleteTaskDialogOpened = true
                },
                onCheckboxClick = {
                    onEvent(OnTaskEvent.OnIsCompleteChange)
                }
            )
        }) { paddingValues ->

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.title,
                onValueChange = { onEvent(OnTaskEvent.OnTitleChange(it)) },
                label = { Text(text = "Title") },
                singleLine = true,
                isError = titleError != null && state.title.isNotEmpty(),
                supportingText = { Text(text = titleError.orEmpty()) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.description,
                onValueChange = { onEvent(OnTaskEvent.OnDescriptionChange(it)) },
                label = { Text(text = "Description") },
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(text = "Due date", style = MaterialTheme.typography.bodyMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = state.dueDate.changeMillisToDateString(),
                    style = MaterialTheme.typography.bodyLarge
                )

                IconButton(onClick = { datePickerDialogOpened = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date Selector"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = state.priority.title, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                enumValues<SubjectPriority>().forEach { priority ->
                    PriorityButton(
                        modifier = Modifier.weight(1f),
                        label = priority.title,
                        backgroundColor = priority.color,
                        borderColor = if (priority == state.priority) Color.White else Color.Transparent,
                        labelColor = if (priority == state.priority) Color.White else Color.White.copy(
                            alpha = 0.7f
                        ),
                        onClick = { onEvent(OnTaskEvent.OnPriorityChange(priority)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(text = "Select Subject", style = MaterialTheme.typography.bodyMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                val defaultSubject = state.subjects.firstOrNull()?.name
                    ?: StudyConstants.UtilityConstant.EMPTY_STRING
                Text(
                    text = state.relatedToSubject ?: defaultSubject,
                    style = MaterialTheme.typography.bodyLarge
                )

                IconButton(onClick = { isBottomSheetOpen = true }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Subject"
                    )
                }
            }

            Button(
                enabled = titleError == null,
                onClick = { onEvent(OnTaskEvent.SaveTask) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Text(text = "Save")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskScreenTopBar(
    isTaskExists: Boolean,
    isCompleted: Boolean,
    checkBoxBorderColor: Color,
    onBackButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onCheckboxClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = "Task Screen",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Navigate Back")
            }
        },
        actions = {
            if (isTaskExists) {
                TaskCheckBox(
                    isComplete = isCompleted,
                    borderColor = checkBoxBorderColor,
                    onCheckBoxClick = onCheckboxClick
                )

                IconButton(onClick = onDeleteButtonClick) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Task")
                }
            }
        })
}

@Composable
private fun PriorityButton(
    modifier: Modifier,
    label: String,
    backgroundColor: Color,
    borderColor: Color,
    labelColor: Color,
    onClick: () -> Unit,
) {

    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(5.dp)
            .border(1.dp, borderColor, RoundedCornerShape(5.dp))
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = labelColor)
    }

}