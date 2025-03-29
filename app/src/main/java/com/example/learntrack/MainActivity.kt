package com.example.learntrack

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.example.learntrack.domain.model.Session
import com.example.learntrack.domain.model.Task
import com.example.learntrack.presentation.dashboard.DashboardScreenRoute
import com.example.learntrack.presentation.navigation.DashBoardScreenNavigation
import com.example.learntrack.presentation.navigation.SessionScreenNavigation
import com.example.learntrack.presentation.navigation.SubjectScreenNavigation
import com.example.learntrack.presentation.navigation.TaskScreenNavigation
import com.example.learntrack.presentation.session.SessionScreenRoute
import com.example.learntrack.presentation.session.StudySessionTimerService
import com.example.learntrack.presentation.subject.SubjectScreenRoute
import com.example.learntrack.presentation.task.TaskScreenRoute
import com.example.learntrack.presentation.theme.LearnTrackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isBound by mutableStateOf(false)
    private lateinit var timerService: StudySessionTimerService

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(
            p0: ComponentName?,
            p1: IBinder?,
        ) {
            val binder = p1 as StudySessionTimerService.StudySessionTimerBinder
            timerService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            if (isBound) {
                LearnTrackTheme {
                    EduLearnApplication(intent, timerService)
                }
            }
        }
        requestPermission()
    }

    override fun onStart() {
        super.onStart()
        Intent(this, StudySessionTimerService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }
}

@Composable
fun EduLearnApplication(intent: Intent, timerService: StudySessionTimerService) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        intent.data?.let { uri ->
            navController.handleDeepLink(Intent().apply { data = uri })
        }
    }

    NavHost(navController = navController, startDestination = DashBoardScreenNavigation) {
        composable<DashBoardScreenNavigation> {
            DashboardScreenRoute(navController)
        }

        composable<SessionScreenNavigation>(
            deepLinks = listOf(navDeepLink { uriPattern = "edu_learn://dashboard/session" })
        ) {
            val args = it.toRoute<SessionScreenNavigation>()
            SessionScreenRoute(navController, timerService)
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
