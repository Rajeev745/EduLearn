package com.example.learntrack.presentation.session

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.learntrack.dummySessions
import com.example.learntrack.presentation.components.DeleteDialog
import com.example.learntrack.presentation.components.studySessionList
import com.example.learntrack.presentation.session.StudySessionServiceHelper.triggerForegroundService
import com.example.learntrack.utils.StudyConstants.ServiceConstants.ACTION_SERVICE_CANCEL
import com.example.learntrack.utils.StudyConstants.ServiceConstants.ACTION_SERVICE_START
import com.example.learntrack.utils.StudyConstants.ServiceConstants.ACTION_SERVICE_STOP

@Composable
fun SessionScreenRoute(navController: NavHostController) {

    val sessionViewModel: SessionViewModel = hiltViewModel()

    SessionScreen(onBackButtonClicked = { navController.popBackStack() })
}

@Composable
fun SessionScreen(onBackButtonClicked: () -> Unit) {

    val context = LocalContext.current

    var deleteSessionDialogOpened by rememberSaveable {
        mutableStateOf(false)
    }

    DeleteDialog(
        isOpen = deleteSessionDialogOpened,
        title = "Delete session",
        description = "Are you sure you want to delete this session?",
        onDismissRequestClick = { deleteSessionDialogOpened = false },
        onConfirmButtonClick = { deleteSessionDialogOpened = false }
    )

    Scaffold(topBar = {
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
                        .aspectRatio(1f)
                )
            }

            item {
                RelatedToSubject(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    relatedToSubject = "English",
                    selectSubjectButtonClick = {}
                )
            }

            item {
                SessionButtons(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onStartButtonClick = {
                        triggerForegroundService(
                            context = context,
                            action = ACTION_SERVICE_START
                        )
                    },
                    onStopButtonClick = {
                        triggerForegroundService(
                            context = context,
                            action = ACTION_SERVICE_STOP
                        )
                    },
                    onCancelButtonClick = {
                        triggerForegroundService(
                            context = context,
                            action = ACTION_SERVICE_CANCEL
                        )
                    }
                )
            }

            studySessionList(
                sectionTitle = "STUDY SESSION HISTORY",
                sessions = dummySessions,
                emptyListText = "You don't have any recent study history",
                onDeleteSessionClick = {
                    deleteSessionDialogOpened = true
                }
            )
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
private fun TimerSection(modifier: Modifier = Modifier) {

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .border(5.dp, color = MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
        )
        Text(
            text = "01:59:34",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 40.sp)
        )
    }

}

@Composable
private fun RelatedToSubject(
    modifier: Modifier,
    relatedToSubject: String,
    selectSubjectButtonClick: () -> Unit,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "Select Subject", style = MaterialTheme.typography.bodyMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = relatedToSubject, style = MaterialTheme.typography.bodyLarge)

            IconButton(onClick = selectSubjectButtonClick) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select Subject"
                )
            }
        }
    }
}

@Composable
private fun SessionButtons(
    modifier: Modifier,
    onStartButtonClick: () -> Unit,
    onStopButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit,
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Button(onClick = onStopButtonClick) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Stop",
                style = MaterialTheme.typography.labelMedium
            )
        }

        Button(onClick = onStartButtonClick) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Start",
                style = MaterialTheme.typography.labelMedium
            )
        }

        Button(onClick = onCancelButtonClick) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Cancel",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}