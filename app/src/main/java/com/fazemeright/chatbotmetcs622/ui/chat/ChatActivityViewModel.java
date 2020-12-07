package com.fazemeright.chatbotmetcs622.ui.chat;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.fazemeright.chatbotmetcs622.database.messages.Message;
import com.fazemeright.chatbotmetcs622.models.ChatRoom;
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository;
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel;
import com.fazemeright.firebase_api_library.api.result.Result;
import java.util.List;

public class ChatActivityViewModel extends BaseViewModel {
  private final MutableLiveData<Result<Boolean>> _messageSent = new MutableLiveData<>();
  public LiveData<Result<Boolean>> messageSent = _messageSent;
  public LiveData<Message> messages;

  public ChatActivityViewModel(@NonNull Application application) {
    super(application);
  }

  public LiveData<List<Message>> getMessagesForChatRoom(ChatRoom chatRoom) {
    return mMessageRepository.getMessagesForChatRoom(chatRoom);
  }

  public void clearAllChatRoomMessages(ChatRoom chatRoom) {
    runOnThread(() -> mMessageRepository.clearAllChatRoomMessages(chatRoom));
  }

  public void sendNewMessage(Context mContext, Message newMessage) {
    runOnThread(() -> mMessageRepository.newMessageSent(mContext, newMessage,
        new MessageRepository.OnMessageResponseReceivedListener() {
          @Override
          public void onMessageResponseReceived(Message response) {
            _messageSent.setValue(Result.withData(true));
          }

          @Override
          public void onNoResponseReceived(Error error) {
            _messageSent.setValue(Result.exception(new Exception(error.getMessage())));
          }
        }));
  }
}
