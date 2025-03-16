package com.example.learntrack.presentation.session

import android.content.Context
import android.content.Intent

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
}