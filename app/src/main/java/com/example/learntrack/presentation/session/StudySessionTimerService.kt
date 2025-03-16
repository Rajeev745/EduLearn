package com.example.learntrack.presentation.session

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.example.learntrack.utils.StudyConstants
import com.example.learntrack.utils.StudyConstants.ServiceConstants.ACTION_SERVICE_CANCEL
import com.example.learntrack.utils.StudyConstants.ServiceConstants.ACTION_SERVICE_START
import com.example.learntrack.utils.StudyConstants.ServiceConstants.ACTION_SERVICE_STOP
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StudySessionTimerService @Inject constructor(
    private val notificationManager: NotificationManager,
) : Service() {

    companion object {
        const val TAG = "StudySessionTimerService"
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.action?.let { serviceAction ->
            when (serviceAction) {
                ACTION_SERVICE_START -> {
                    startForegroundService()
                }

                ACTION_SERVICE_STOP -> {

                }

                ACTION_SERVICE_CANCEL -> {

                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                StudyConstants.NotificationConstants.NOTIFICATION_CHANNEL_ID,
                StudyConstants.NotificationConstants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}