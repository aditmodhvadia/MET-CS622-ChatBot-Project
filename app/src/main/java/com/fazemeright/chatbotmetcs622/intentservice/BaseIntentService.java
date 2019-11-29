package com.fazemeright.chatbotmetcs622.intentservice;

import android.content.Intent;
import android.os.PowerManager;

import androidx.annotation.Nullable;

import com.fazemeright.chatbotmetcs622.database.ChatBotDatabase;

import timber.log.Timber;


public abstract class BaseIntentService extends android.app.IntentService {
    /**
     * TAG for logs
     */
    private static final String TAG = "BaseIntentService";
    /**
     * Use to send data with intent
     */
    public static final String ACTION = "IntentAction";
    public static final String MESSAGE = "Message";
    public static final String RESULT_RECEIVER = "ResultReceiver";

    protected ChatBotDatabase database;

    private PowerManager.WakeLock wakeLock;

    public abstract int getWakeLockTimeoutInSeconds();

    protected abstract void onHandleNewIntent(Intent intent);

    /**
     * Creates an BaseIntentService.  Invoked by your subclass's constructor.
     */
    public BaseIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.i("onCreate");
        database = ChatBotDatabase.getInstance(getApplicationContext());
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ChatBot:WakeLockTag");
            wakeLock.acquire(getWakeLockTimeoutInSeconds() * 1000);    //  acquire CPU
            Timber.i("onCreate: Wake Lock acquired");
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Timber.d("onHandleIntent");
        if(intent != null){
            onHandleNewIntent(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.i("onDestroy");
        wakeLock.release();
        Timber.i("onDestroy: Wake Lock released");
    }
}
