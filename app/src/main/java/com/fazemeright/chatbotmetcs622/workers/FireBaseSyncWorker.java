package com.fazemeright.chatbotmetcs622.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.fazemeright.chatbotmetcs622.database.messages.Message;
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository;

public class FireBaseSyncWorker extends Worker {
  public FireBaseSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
    super(context, workerParams);
  }

  @NonNull
  @Override
  public Result doWork() {
    //        add all messages from Room to FireBase
    MessageRepository messageRepository = MessageRepository.getInstance(getApplicationContext());
    for (Message message : messageRepository.getAllMessages()) {
      messageRepository.addMessageToFireBase(Message.getHashMap(message));
    }
    //        get all messages from FireBase to Room
    messageRepository.syncMessagesFromFireStoreToRoom();

    return Result.success();
  }
}
