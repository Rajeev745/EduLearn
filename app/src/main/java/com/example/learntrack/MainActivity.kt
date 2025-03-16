package com.example.learntrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.learntrack.domain.model.Session
import com.example.learntrack.domain.model.Task
import com.example.learntrack.presentation.dashboard.DashboardScreenRoute
import com.example.learntrack.presentation.navigation.DashBoardScreenNavigation
import com.example.learntrack.presentation.navigation.SessionScreenNavigation
import com.example.learntrack.presentation.navigation.SubjectScreenNavigation
import com.example.learntrack.presentation.navigation.TaskScreenNavigation
import com.example.learntrack.presentation.session.SessionScreenRoute
import com.example.learntrack.presentation.subject.SubjectScreenRoute
import com.example.learntrack.presentation.task.TaskScreenRoute
import com.example.learntrack.presentation.theme.LearnTrackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnTrackTheme {
                EduLearnApplication()
            }
        }
    }
}

@Composable
fun EduLearnApplication() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = DashBoardScreenNavigation) {
        composable<DashBoardScreenNavigation> {
            DashboardScreenRoute(navController)
        }

        composable<SessionScreenNavigation> {
            val args = it.toRoute<SessionScreenNavigation>()
            SessionScreenRoute(navController)
        }

        composable<TaskScreenNavigation> {
            val args = it.toRoute<TaskScreenNavigation>()
            TaskScreenRoute(navController)
        }

        composable<SubjectScreenNavigation> {
            val args = it.toRoute<SubjectScreenNavigation>()
            SubjectScreenRoute(navController, args)
        }
    }
}

val dummySessions = listOf(
    Session(
        sessionSubjectId = 101,
        relatedToSubject = "Mathematics",
        date = System.currentTimeMillis() - 86400000, // 1 day ago
        duration = 3600000, // 1 hour in milliseconds
        sessionId = 1
    ),
    Session(
        sessionSubjectId = 102,
        relatedToSubject = "Physics",
        date = System.currentTimeMillis() - 172800000, // 2 days ago
        duration = 5400000, // 1.5 hours in milliseconds
        sessionId = 2
    ),
    Session(
        sessionSubjectId = 103,
        relatedToSubject = "Chemistry",
        date = System.currentTimeMillis() - 259200000, // 3 days ago
        duration = 2700000, // 45 minutes in milliseconds
        sessionId = 3
    ),
    Session(
        sessionSubjectId = 104,
        relatedToSubject = "Biology",
        date = System.currentTimeMillis() - 345600000, // 4 days ago
        duration = 7200000, // 2 hours in milliseconds
        sessionId = 4
    ),
    Session(
        sessionSubjectId = 105,
        relatedToSubject = "History",
        date = System.currentTimeMillis() - 432000000, // 5 days ago
        duration = 1800000, // 30 minutes in milliseconds
        sessionId = 5
    )
)


val dummyTaskList = listOf<Task>(
    Task(
        title = "Preparing for exams",
        description = "",
        priority = 0,
        isComplete = false,
        dueDate = 0L,
        relatedToSubject = "",
        taskSubjectId = 0,
        taskId = 0
    ),
    Task(
        title = "Preparing for exams",
        description = "",
        priority = 1,
        isComplete = false,
        dueDate = 0L,
        relatedToSubject = "",
        taskSubjectId = 2,
        taskId = 2
    ),
    Task(
        title = "Preparing for exams",
        description = "",
        priority = 0,
        isComplete = false,
        dueDate = 0L,
        relatedToSubject = "",
        taskSubjectId = 2,
        taskId = 2
    ),
    Task(
        title = "Preparing for exams",
        description = "",
        priority = 2,
        isComplete = true,
        dueDate = 0L,
        relatedToSubject = "",
        taskSubjectId = 3,
        taskId = 3
    )
)
