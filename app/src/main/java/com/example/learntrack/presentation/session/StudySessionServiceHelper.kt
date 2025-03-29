package com.example.learntrack.presentation.session

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.example.learntrack.MainActivity
import com.example.learntrack.utils.StudyConstants.NotificationConstants.CLICK_REQUEST_CODE

object StudySessionServiceHelper {

    /**
     * Triggers the StudySessionTimerService foreground service with the specified action.
     *
     * @param context The context used to start the service
     * @param action The action string that defines what operation the service should perform
     */
    fun triggerForegroundService(context: Context, action: String) {
        Intent(context, StudySessionTimerService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }

    fun clickPendingIntent(context: Context): PendingIntent {
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "edu_learn://dashboard/session".toUri(),
            context,
            MainActivity::class.java
        )

        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(
                CLICK_REQUEST_CODE,
                PendingIntent.FLAG_IMMUTABLE
            ) as PendingIntent
        }
    }
}