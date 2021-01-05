package com.fazemeright.chatbotmetcs622.intentservice

import android.app.IntentService
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.fazemeright.chatbotmetcs622.ChatBotApp
import com.fazemeright.chatbotmetcs622.R
import com.fazemeright.chatbotmetcs622.database.ChatBotDatabase
import com.fazemeright.chatbotmetcs622.database.ChatBotDatabase.Companion.getInstance
import com.fazemeright.chatbotmetcs622.database.message.Message
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository
import timber.log.Timber

open class FireBaseIntentService
/**
 * Creates an IntentService. Invoked by your subclass's constructor.
 *
 *
 * TAG Used to name the worker thread, important only for debugging.
 */
    : IntentService(TAG) {
    protected var database: ChatBotDatabase? = null
    private lateinit var wakeLock: PowerManager.WakeLock
    private var messageRepository: MessageRepository? = null
    override fun onCreate() {
        super.onCreate()
        Timber.i("onCreate")
        database = getInstance(applicationContext)
        getWakeLock()
        showForegroundServiceNotification()
    }

    private fun getWakeLock() {
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ChatBot:WakeLockTag").apply {
            acquire(60_000) //  acquire CPU
        }
        Timber.i("onCreate: Wake Lock acquired")
    }

    override fun onDestroy() {
        super.onDestroy()
        wakeLock.release()
    }

    override fun onHandleIntent(intent: Intent?) {
        Timber.d("onHandleIntent")
        if (intent != null) {
            when (intent.getStringExtra(ACTION_INTENT)) {
                Actions.ACTION_ADD_MESSAGE.name -> addMessageToFireStore(intent.getSerializableExtra(MESSAGE) as Message)
                Actions.ACTION_SYNC_MESSAGES.name -> syncMessages()
            }
        }
    }

    /**
     * Call to sync messages from FireStore to Room for the logged in user.
     */
    private fun syncMessages() {
        messageRepository?.syncMessagesFromFireStoreToRoom()
    }

    /**
     * Call to add given message to FireStore.
     *
     * @param message given message
     */
    private fun addMessageToFireStore(message: Message) {
        messageRepository?.addMessageToFireBase(message)
    }

    /**
     * Call to display foreground running notification to notify user of a background operation
     * running.
     */
    private fun showForegroundServiceNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, ChatBotApp.CHANNEL_ID).apply {
                setContentTitle("Chat Bot")
                setContentText("Syncing Messages...")
                setSmallIcon(R.drawable.ic_launcher_foreground)
                priority = NotificationManager.IMPORTANCE_DEFAULT
            }.build()
                    .also {
                        startForeground(1, it)
                    }
        }
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        messageRepository = MessageRepository.getInstance(applicationContext)
    }

    enum class Actions {
        ACTION_ADD_MESSAGE,
        ACTION_SYNC_MESSAGES,
    }

    companion object {
        const val MESSAGE = "Message"
        const val RESULT_RECEIVER = "ResultReceiver"
        const val ACTION_INTENT = "IntentAction"

        /**
         * TAG for logs.
         */
        private const val TAG = "FireBaseIntentService"
    }
}