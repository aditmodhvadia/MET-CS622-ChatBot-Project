package com.fazemeright.chatbotmetcs622.intentservice;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.fazemeright.chatbotmetcs622.ChatBotApp;
import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.database.ChatBotDatabase;
import com.fazemeright.chatbotmetcs622.database.messages.Message;
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository;
import timber.log.Timber;

public class FireBaseIntentService extends IntentService {

  public static final String ACTION_ADD_MESSAGE = "AddMessage";
  public static final String ACTION_SYNC_MESSAGES = "SyncMessages";
  /**
   * Use to send data with intent
   */
  public static final String ACTION = "IntentAction";
  public static final String MESSAGE = "Message";
  public static final String RESULT_RECEIVER = "ResultReceiver";
  /**
   * TAG for logs
   */
  private static final String TAG = "FireBaseIntentService";
  protected ChatBotDatabase database;
  private PowerManager.WakeLock wakeLock;
  private MessageRepository messageRepository;

  /**
   * Creates an IntentService. Invoked by your subclass's constructor.
   *
   * <p>TAG Used to name the worker thread, important only for debugging.
   */
  public FireBaseIntentService() {
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
      wakeLock.acquire(60 * 1000); //  acquire CPU
      Timber.i("onCreate: Wake Lock acquired");
    }
    showForegroundServiceNotification("Chat Bot", "Syncing Messages...");
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Timber.i("onDestroy");
    wakeLock.release();
    Timber.i("onDestroy: Wake Lock released");
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    Timber.d("onHandleIntent");
    if (intent != null) {
      switch (intent.getStringExtra(ACTION)) {
        case ACTION_ADD_MESSAGE:
          addMessageToFireStore((Message) intent.getSerializableExtra(MESSAGE));
          break;
        case ACTION_SYNC_MESSAGES:
          syncMessages();
          break;
      }
    }
  }

  /**
   * Call to sync messages from FireStore to Room for the logged in user
   */
  private void syncMessages() {
    /*try {
        Thread.sleep(5000); //  intentionally kept delay to show in presentation TODO: Remove afterwards
    } catch (InterruptedException e) {
        e.printStackTrace();
    }*/
    messageRepository.syncMessagesFromFireStoreToRoom();
  }

  /**
   * Call to add given message to FireStore
   *
   * @param message given message
   */
  private void addMessageToFireStore(Message message) {
    messageRepository.addMessageToFireBase(Message.getHashMap(message));
  }

  /**
   * Call to display foreground running notification to notify user of a background operation
   * running
   *
   * @param title title to display in notification
   * @param text  text to display in notification
   */
  private void showForegroundServiceNotification(String title, String text) {
    Timber.i("Show notification called");
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

      Notification notification =
          new NotificationCompat.Builder(this, ChatBotApp.CHANNEL_ID)
              .setContentTitle(title)
              .setContentText(text)
              .setSmallIcon(R.drawable.ic_launcher_foreground)
              .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
              .build();

      startForeground(1, notification);
    }
  }

  @Override
  public void onStart(@Nullable Intent intent, int startId) {
    super.onStart(intent, startId);
    messageRepository = MessageRepository.getInstance(getApplicationContext());
  }
}
