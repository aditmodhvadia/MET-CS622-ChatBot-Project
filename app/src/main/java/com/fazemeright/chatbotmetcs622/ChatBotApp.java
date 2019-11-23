package com.fazemeright.chatbotmetcs622;

import android.app.Application;

import com.fazemeright.chatbotmetcs622.network.NetworkManager;

import timber.log.Timber;

public class ChatBotApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        NetworkManager.getInstance().init(getApplicationContext(), 300);
    }
}
