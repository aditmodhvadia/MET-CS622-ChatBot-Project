package com.fazemeright.chatbotmetcs622.workers;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository;

public class FireBaseSyncWorker extends Worker {
  public FireBaseSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
    super(context, workerParams);
  }

  @NonNull
  @Override
  public Result doWork() {
    syncMessagesWithCloudAndLocal();
    return Result.success();
  }

  /**
   * Sync messages from local to cloud, and then from cloud to local
   */
  private void syncMessagesWithCloudAndLocal() {
    final MessageRepository messageRepository =
        MessageRepository.getInstance(getApplicationContext());
    messageRepository.addMessagesToFireBase(messageRepository.getAllMessages());

    messageRepository.syncMessagesFromFireStoreToRoom();
  }
}
