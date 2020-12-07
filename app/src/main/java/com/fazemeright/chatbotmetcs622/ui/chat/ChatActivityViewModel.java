package com.fazemeright.chatbotmetcs622.ui.chat;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.fazemeright.chatbotmetcs622.database.message.Message;
import com.fazemeright.chatbotmetcs622.models.ChatRoom;
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel;
import com.fazemeright.library.api.result.Result;
import com.fazemeright.library.api.result.ResultAdapterForBooleanLiveUpdates;
import java.util.List;

public class ChatActivityViewModel extends BaseViewModel {
  private final MutableLiveData<Result<Boolean>> messageSentMutable = new MutableLiveData<>();
  public LiveData<Result<Boolean>> messageSent = messageSentMutable;

  public ChatActivityViewModel(@NonNull Application application) {
    super(application);
  }

  public LiveData<List<Message>> getMessagesForChatRoom(ChatRoom chatRoom) {
    return messageRepository.getMessagesForChatRoom(chatRoom);
  }

  /**
   * Clear all messages for the chat room.
   *
   * @param chatRoom chat room
   */
  public void clearAllChatRoomMessages(ChatRoom chatRoom) {
    runOnThread(() -> messageRepository.clearAllChatRoomMessages(chatRoom));
  }

  /**
   * Send new message.
   * Store in local database.
   *
   * @param context    context
   * @param newMessage message
   */
  public void sendNewMessage(Context context, Message newMessage) {
    runOnThread(() -> messageRepository.newMessageSent(context, newMessage,
        new ResultAdapterForBooleanLiveUpdates<>(messageSentMutable)));
  }
}
