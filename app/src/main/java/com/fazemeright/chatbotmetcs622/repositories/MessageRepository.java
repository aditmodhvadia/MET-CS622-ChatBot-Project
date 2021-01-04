package com.fazemeright.chatbotmetcs622.repositories;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.fazemeright.chatbotmetcs622.database.ChatBotDatabase;
import com.fazemeright.chatbotmetcs622.database.message.Message;
import com.fazemeright.chatbotmetcs622.intentservice.FireBaseIntentService;
import com.fazemeright.chatbotmetcs622.models.ChatRoom;
import com.fazemeright.chatbotmetcs622.network.ApiManager;
import com.fazemeright.chatbotmetcs622.network.NetworkManager;
import com.fazemeright.chatbotmetcs622.network.handlers.NetworkCallback;
import com.fazemeright.chatbotmetcs622.network.models.NetError;
import com.fazemeright.chatbotmetcs622.network.models.NetResponse;
import com.fazemeright.chatbotmetcs622.network.models.response.QueryResponseMessage;
import com.fazemeright.library.api.DatabaseStore;
import com.fazemeright.library.api.Storable;
import com.fazemeright.library.api.UserAuthResult;
import com.fazemeright.library.api.UserAuthentication;
import com.fazemeright.library.api.firebase.FireBaseDatabaseStore;
import com.fazemeright.library.api.firebase.FireBaseUserAuthentication;
import com.fazemeright.library.api.result.Result;
import com.fazemeright.library.listeners.OnTaskCompleteListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import timber.log.Timber;

public class MessageRepository {

  private static MessageRepository repository;
  private final ChatBotDatabase database;
  private final ApiManager apiManager;
  private final UserAuthentication userAuthentication;
  private final DatabaseStore onlineDatabaseStore;

  private MessageRepository(
      ChatBotDatabase database, ApiManager apiManager,
      UserAuthentication userAuthentication, DatabaseStore onlineDatabaseStore) {
    this.database = database;
    this.apiManager = apiManager;
    this.userAuthentication = userAuthentication;
    this.onlineDatabaseStore = onlineDatabaseStore;
  }

  /**
   * Call to get instance of MessageRepository with the given context.
   *
   * @param context given context
   * @return synchronized call to get Instance of MessageRepository class
   */
  public static MessageRepository getInstance(Context context) {
    if (repository == null) {
      synchronized (MessageRepository.class) {
        ChatBotDatabase database = ChatBotDatabase.getInstance(context);
        ApiManager apiManager = ApiManager.getInstance();
        UserAuthentication userAuthentication = FireBaseUserAuthentication.getInstance();
        DatabaseStore databaseStore = FireBaseDatabaseStore.getInstance();
        apiManager.init(NetworkManager.getInstance());
        repository =
            new MessageRepository(database, apiManager, userAuthentication,
                databaseStore);
      }
    }
    return repository;
  }

  /**
   * Call to insert given message into database with thread safety.
   *
   * @param newMessage given project
   * @param listener   task completion listener
   */
  private void insertMessageInRoom(Message newMessage,
                                   @Nullable OnTaskCompleteListener<Message> listener) {
    Timber.i("Insert message in Room called%s", newMessage.getMsg());
    database.messageDao().insert(newMessage);
    Message insertedMsg = database.messageDao().getLatestMessage(newMessage.getChatRoomId());

    if (listener != null) {
      listener.onComplete(Result.withData(insertedMsg));
    }
  }

  /**
   * Register new user and store the details.
   *
   * @param userEmail              email
   * @param password               password
   * @param firstName              first name
   * @param lastName               last name
   * @param onTaskCompleteListener task completion listener
   */
  public void createNewUserAndStoreDetails(final String userEmail,
                                           final String password,
                                           final String firstName,
                                           final String lastName,
                                           @Nullable final OnTaskCompleteListener<Void>
                                               onTaskCompleteListener) {
    userAuthentication.createNewUserWithEmailPassword(userEmail, password, result -> {
      if (result.isSuccessful()) {
        onlineDatabaseStore.storeUserData(
            Objects.requireNonNull(userAuthentication.getCurrentUserUid()),
            getStorableFromUserDetails(userEmail, firstName, lastName));
        onTaskCompleteListener.onComplete(Result.nullResult());
      } else {
        onTaskCompleteListener.onComplete(Result.exception(result.getException()));
      }
    });
  }

  @NonNull
  private Storable getStorableFromUserDetails(String userEmail, String firstName,
                                              String lastName) {
    return new Storable() {
      @Nonnull
      @Override
      public Map<String, Object> getHashMap() {
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("emailAddress", userEmail);
        userProfile.put("firstName", firstName);
        userProfile.put("lastName", lastName);
        return userProfile;
      }

      @Override
      public long getId() {
        return 0;
      }
    };
  }

  /**
   * Call to update given project into database with thread safety.
   *
   * @param oldMessage given project
   */
  private void updateMessage(Message oldMessage) {
    //        insert into Room using AsyncTask
    database.messageDao().update(oldMessage);
  }

  /**
   * Call to get Message with given Message ID.
   *
   * @param messageId given Message ID
   * @return Message with given ID
   */
  public Message getMessage(long messageId) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * Call to delete message with given message.
   *
   * @param message given Message
   */
  public void deleteMessage(Message message) {
    runOnThread(() -> database.messageDao().deleteItem(message));
  }

  /**
   * Get messages from the given chat room.
   *
   * @param chatRoom chat room
   * @return List of Messages
   */
  public LiveData<List<Message>> getMessagesForChatRoom(ChatRoom chatRoom) {
    return getChatRoomMessagesFromDatabase(chatRoom);
  }

  private LiveData<List<Message>> getChatRoomMessagesFromDatabase(ChatRoom chatRoom) {
    return database.messageDao().getAllMessagesFromChatRoomLive(chatRoom.getId());
  }

  /**
   * Called when user sends a given new message in the ChatRoom - Add new Message to Room - Call API
   * to fetch answer for new message - Sync message with FireStore.
   *
   * @param newMessage given new message
   */
  public void newMessageSent(
      final Context context,
      final Message newMessage,
      @Nullable final OnTaskCompleteListener<Message> listener) {
    runOnThread(() -> insertMessageInRoom(newMessage, result -> {
      final Message roomLastMessage = result.getData();
      insertMessageInFireBase(context, roomLastMessage);
      apiManager.queryDatabase(
          context,
          newMessage,
          new NetworkCallback<QueryResponseMessage>() {
            @Override
            public void onSuccess(NetResponse<QueryResponseMessage> response) {
              Message queryResponseMessage =
                  Message.newMessage(
                      response.getResponse().getData().getResponseMsg(),
                      newMessage.getReceiver(),
                      newMessage.getSender(),
                      newMessage.getChatRoomId());

              runOnThread(() -> insertMessageInRoom(queryResponseMessage, result -> {
                Message roomLastInsertedMessage = result.getData();
                insertMessageInFireBase(context, roomLastInsertedMessage);
                if (listener != null) {
                  listener.onComplete(Result.withData(queryResponseMessage));
                }
              }));
            }

            @Override
            public void onError(NetError error) {
              if (listener != null) {
                listener.onComplete(Result.exception(error));
              }
            }
          });
    }));
  }

  /**
   * Call to insert the given new message to FireStore database.
   *
   * @param context    context
   * @param newMessage given new message
   */
  private void insertMessageInFireBase(Context context, Message newMessage) {
    Intent intent = new Intent(context, FireBaseIntentService.class);
    intent.putExtra(FireBaseIntentService.Actions.ACTION,
        FireBaseIntentService.Actions.ACTION_ADD_MESSAGE);
    intent.putExtra(FireBaseIntentService.MESSAGE, newMessage);
    context.startService(intent);
  }

  /**
   * Get all messages stored inside local database.
   *
   * @return <code>List</code> of  messages
   */
  public ArrayList<Message> getAllMessages() {
    return (ArrayList<Message>) database.messageDao().getAllMessages();
  }

  /**
   * Clear all given chat room messages - From Room - From FireStore.
   *
   * @param chatRoom given chat room
   */
  public void clearAllChatRoomMessages(ChatRoom chatRoom) {
    database.messageDao().clearChatRoomMessages(chatRoom.getId());
  }

  /**
   * Call to logout user and clear all messages from Room.
   */
  public void logOutUser() {
    userAuthentication.signOutUser();
    clearAllMessages();
  }

  /**
   * Call to clear all messages from Room.
   */
  private void clearAllMessages() {
    runOnThread(() -> database.messageDao().clear());
  }

  /**
   * Add given list of messages to Room.
   */
  public void addMessagesToLocal(List<Message> messages) {
    runOnThread(() -> database.messageDao().insertAllMessages(messages));
  }

  public void runOnThread(Runnable runnable) {
    new Thread(runnable).start();
  }

  /**
   * Call to add the given message to FireStore.
   *
   * @param message given message
   */
  public void addMessageToFireBase(Message message) {
    this.onlineDatabaseStore
        .storeMessage(message, this.userAuthentication.getCurrentUserUid());
  }

  /**
   * Call to add the given messages to FireStore.
   *
   * @param messageList given messages list
   */
  public void addMessagesToFireBase(List<Message> messageList) {
    for (Message message : messageList) {
      this.addMessageToFireBase(message);
    }
  }

  /**
   * Call to sync messages from FireStore to Room for the logged in user.
   */
  public void syncMessagesFromFireStoreToRoom() {
    onlineDatabaseStore.getAllMessagesForUser(userAuthentication.getCurrentUserUid(), result -> {
      if (result.isSuccessful()) {
        List<Message> messages = new ArrayList<>();
        for (Map<String, Object> data : result.getData()) {
          Timber.i(String.valueOf(data.get("mid")));
          messages.add(Message.fromMap(data));
        }
        addMessagesToLocal(messages);
      }
    });
  }

  /**
   * Get user name of the current user.
   *
   * @return user name
   */
  @Nullable
  public String getUserName() {
    return userAuthentication.getUserName();
  }

  /**
   * Sign in user with email and password.
   *
   * @param email    email
   * @param password password
   * @param listener task completion listener`
   */
  public void signInWithEmailAndPassword(String email, String password,
                                         OnTaskCompleteListener<UserAuthResult> listener) {
    userAuthentication.signInWithEmailAndPassword(email, password, listener);
  }

  /**
   * Reload the current user authentication state.
   *
   * @param listener listener for updates
   */
  public void reloadCurrentUserAuthState(
      OnTaskCompleteListener<Void> listener) {
    userAuthentication.reloadCurrentUserAuthState(listener);
  }
}
