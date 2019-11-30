package com.fazemeright.chatbotmetcs622;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.fazemeright.chatbotmetcs622.network.NetworkManager;
import com.fazemeright.chatbotmetcs622.workers.FireBaseSyncWorker;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class ChatBotApp extends Application {

    public static final String CHANNEL_ID = "FireBaseSyncChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        NetworkManager.getInstance().init(getApplicationContext(), 300);

        createNotificationChannel();
    }

    /**
     * Call to create a notification channel for OS greater than OREO
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "FireBase Sync channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}
