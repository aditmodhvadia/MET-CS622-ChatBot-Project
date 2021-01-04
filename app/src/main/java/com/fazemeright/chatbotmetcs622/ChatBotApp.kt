package com.fazemeright.chatbotmetcs622

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.fazemeright.chatbotmetcs622.network.ApiManager
import com.fazemeright.chatbotmetcs622.network.NetworkManager
import timber.log.Timber
import timber.log.Timber.DebugTree

class ChatBotApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        NetworkManager.instance?.init(applicationContext, 300)
        ApiManager.BaseUrl.setLocalIp("http://192.168.43.28:8080")
        createNotificationChannel()
    }

    /**
     * Call to create a notification channel for OS greater than OREO.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                    CHANNEL_ID, "FireBase Sync channel", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    companion object {
        const val CHANNEL_ID = "FireBaseSyncChannel"
    }
}