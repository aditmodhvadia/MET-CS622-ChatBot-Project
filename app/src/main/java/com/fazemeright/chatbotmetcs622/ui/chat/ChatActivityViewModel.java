package com.fazemeright.chatbotmetcs622.ui.chat;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.fazemeright.chatbotmetcs622.database.message.Message;
import com.fazemeright.chatbotmetcs622.models.ChatRoom;
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel;
import com.fazemeright.firebase_api_library.api.result.Result;
import com.fazemeright.firebase_api_library.api.result.ResultAdapterForBooleanLiveUpdates;
import java.util.List;

public class ChatActivityViewModel extends BaseViewModel {
  private final MutableLiveData<Result<Boolean>> _messageSent = new MutableLiveData<>();
  public LiveData<Result<Boolean>> messageSent = _messageSent;

  public ChatActivityViewModel(@NonNull Application application) {
    super(application);
  }

  public LiveData<List<Message>> getMessagesForChatRoom(ChatRoom chatRoom) {
    return mMessageRepository.getMessagesForChatRoom(chatRoom);
  }

  /**
   * Clear all messages for the chat room
   *
   * @param chatRoom chat room
   */
  public void clearAllChatRoomMessages(ChatRoom chatRoom) {
    runOnThread(() -> mMessageRepository.clearAllChatRoomMessages(chatRoom));
  }

  /**
   * Send new message.
   * Store in local database
   *
   * @param mContext   context
   * @param newMessage message
   */
  public void sendNewMessage(Context mContext, Message newMessage) {
    runOnThread(() -> mMessageRepository.newMessageSent(mContext, newMessage,
        new ResultAdapterForBooleanLiveUpdates<>(_messageSent)));
  }
}
