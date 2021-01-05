package com.fazemeright.chatbotmetcs622

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import timber.log.Timber
import timber.log.Timber.DebugTree

class ChatBotApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        createNotificationChannel()
    }

    /**
     * Call to create a notification channel for OS greater than OREO.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(NotificationChannel(
                    CHANNEL_ID, "FireBase Sync channel", NotificationManager.IMPORTANCE_DEFAULT))
        }
    }

    companion object {
        const val CHANNEL_ID = "FireBaseSyncChannel"
    }
}