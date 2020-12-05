package com.fazemeright.chatbotmetcs622.repositories;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.fazemeright.chatbotmetcs622.database.ChatBotDatabase;
import com.fazemeright.chatbotmetcs622.database.messages.Message;
import com.fazemeright.chatbotmetcs622.database.messages.MessageDao;
import com.fazemeright.chatbotmetcs622.intentservice.FireBaseIntentService;
import com.fazemeright.chatbotmetcs622.models.ChatRoom;
import com.fazemeright.chatbotmetcs622.network.ApiManager;
import com.fazemeright.chatbotmetcs622.network.NetworkManager;
import com.fazemeright.chatbotmetcs622.network.handlers.NetworkCallback;
import com.fazemeright.chatbotmetcs622.network.models.NetError;
import com.fazemeright.chatbotmetcs622.network.models.NetResponse;
import com.fazemeright.chatbotmetcs622.network.models.response.QueryResponseMessage;
import com.fazemeright.firebase_api_library.api.DatabaseStore;
import com.fazemeright.firebase_api_library.api.UserAuthResult;
import com.fazemeright.firebase_api_library.api.UserAuthentication;
import com.fazemeright.firebase_api_library.api.firebase.FireBaseDatabaseStore;
import com.fazemeright.firebase_api_library.api.firebase.FireBaseUserAuthentication;
import com.fazemeright.firebase_api_library.listeners.DBValueListener;

import com.fazemeright.firebase_api_library.listeners.OnTaskCompleteListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;
import timber.log.Timber;

public class MessageRepository {

  private static MessageRepository repository;
  private ChatBotDatabase database;
  private ApiManager apiManager;
  //  private FireBaseApiManager fireBaseApiManager;
  private final UserAuthentication userAuthentication;
  private final DatabaseStore databaseStore;

  private MessageRepository(
      ChatBotDatabase database, ApiManager apiManager,
      UserAuthentication userAuthentication, DatabaseStore databaseStore) {
    this.database = database;
    this.apiManager = apiManager;
//    this.fireBaseApiManager = fireBaseApiManager;
    this.userAuthentication = userAuthentication;
    this.databaseStore = databaseStore;
    //        messageList = this.database.messageDao().getAllMessages();
  }

  /**
   * Call to get instance of MessageRepository with the given context
   *
   * @param context given context
   * @return synchronized call to get Instance of MessageRepository class
   */
  public static MessageRepository getInstance(Context context) {
    if (repository == null) {
      synchronized (MessageRepository.class) {
        ChatBotDatabase database = ChatBotDatabase.getInstance(context);
        ApiManager apiManager = ApiManager.getInstance();
//        FireBaseApiManager fireBaseApiManager = FireBaseApiManager.getInstance();
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

  public UserAuthentication getUserAuthentication() {
    return this.userAuthentication;
  }

  public DatabaseStore getDatabaseStore() {
    return this.databaseStore;
  }

  /**
   * Call to insert given project into database with thread safety
   *
   * @param newMessage given project
   * @return inserted message
   */
  private Message insertMessageInRoom(Message newMessage) {
    //        insert into Room using AsyncTask
    Timber.i("Insert message in Room called%s", newMessage.getMsg());
    try {
      return new InsertAsyncTask(database.messageDao()).execute(newMessage).get();
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
    return newMessage;
  }

  public void createNewUserAndStoreDetails(final String userEmail,
                                           final String password,
                                           final String firstName,
                                           final String lastName,
                                           @Nullable
                                           final OnTaskCompleteListener<Void> onTaskCompleteListener) {
    userAuthentication.createNewUserWithEmailPassword(userEmail, password,
        new OnTaskCompleteListener<UserAuthResult>() {
          @Override
          public void onTaskSuccessful(UserAuthResult result) {
            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("emailAddress", userEmail);
            userProfile.put("firstName", firstName);
            userProfile.put("lastName", lastName);
            databaseStore.storeUserData(
                Objects.requireNonNull(userAuthentication.getCurrentUserUid()), userProfile);
          }

          @Override
          public void onTaskCompleteButFailed(UserAuthResult result) {

          }

          @Override
          public void onTaskFailed(Exception e) {

          }
        });
  }

  /**
   * Call to update given project into database with thread safety
   *
   * @param oldMessage given project
   */
  private void updateMessage(Message oldMessage) {
    //        insert into Room using AsyncTask
    new UpdateAsyncTask(database.messageDao()).execute(oldMessage);
  }

  /**
   * Call to get Message with given Message ID
   *
   * @param mid given Message ID
   * @return Message with given ID
   */
  public Message getMessage(long mid) {
    return fetchMessage(mid);
  }

  /**
   * Call to get Message with given Message ID with thread safety
   *
   * @param pid given Message ID
   * @return Message with given ID
   */
  private Message fetchMessage(long pid) {
    try {
      return new FetchMessageAsyncTask(database.messageDao()).execute(pid).get();
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Call to delete project with given project
   *
   * @param project given Message
   * @return Deleted Message
   */
  public void deleteMessage(Message project) {
    deleteMessageFromRoom(project);
  }

  /**
   * Delete given Message from Room with Thread Safety
   *
   * @param project given Message
   */
  private void deleteMessageFromRoom(Message project) {
    try {
      new DeleteMessageAsyncTask(database.messageDao()).execute(project).get();
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<Message> getMessagesForChatRoom(ChatRoom chatRoom) {
    return getChatRoomMessagesFromDatabase(chatRoom);
  }

  private ArrayList<Message> getChatRoomMessagesFromDatabase(ChatRoom chatRoom) {
    try {
      return (ArrayList<Message>)
          new FetchChatRoomMessagesAsyncTask(database.messageDao()).execute(chatRoom).get();
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  /**
   * Called when user sends a given new message in the ChatRoom - Add new Message to Room - Call API
   * to fetch answer for new message - Sync message with FireStore
   *
   * @param newMessage given new message
   */
  public void newMessageSent(
      final Context context,
      final Message newMessage,
      final OnMessageResponseReceivedListener listener) {
    final Message roomLastMessage = insertMessageInRoom(newMessage);
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

            Message roomLastInsertedMessage = insertMessageInRoom(queryResponseMessage);
            insertMessageInFireBase(context, roomLastInsertedMessage);
            listener.onMessageResponseReceived(queryResponseMessage);
          }

          @Override
          public void onError(NetError error) {
            listener.onNoResponseReceived(new Error(error.getErrorLocalizeMessage()));
          }
        });

    //        TODO: Finish the remaining cart
  }

  /**
   * Call to insert the given new message to FireStore database
   *
   * @param context    context
   * @param newMessage given new message
   */
  private void insertMessageInFireBase(Context context, Message newMessage) {
    Intent intent = new Intent(context, FireBaseIntentService.class);
    intent.putExtra(FireBaseIntentService.ACTION, FireBaseIntentService.ACTION_ADD_MESSAGE);
    intent.putExtra(FireBaseIntentService.MESSAGE, newMessage);
    context.startService(intent);
  }

  public ArrayList<Message> getAllMessages() {
    return (ArrayList<Message>) database.messageDao().getAllMessages();
  }

  /**
   * Clear all given chat room messages - From Room - From FireStore
   *
   * @param chatRoom given chat room
   */
  public void clearAllChatRoomMessages(ChatRoom chatRoom) {
    clearAllChatRoomMessagesFromRoom(chatRoom);
  }

  private void clearAllChatRoomMessagesFromRoom(ChatRoom chatRoom) {
    new ClearAllMessagesInChatRoomAsyncTask(database.messageDao()).execute(chatRoom);
  }

  /**
   * Call to logout user and clear all messages from Room
   */
  public void logOutUser() {
    userAuthentication.signOutUser();
    clearAllMessages();
  }

  /**
   * Call to clear all messages from Room
   */
  private void clearAllMessages() {
    new ClearAllMessagesAsyncTask(database.messageDao()).execute();
  }

  /**
   * Add given list of messages to Room
   *
   * @param messages
   */
  public void addMessages(List<Message> messages) {
    new AddAllMessagesAsyncTask(database.messageDao()).execute(messages);
  }

  /**
   * Call to add the given message to FireStore
   *
   * @param messageHashMap given message converted into HashMap
   */
  public void addMessageToFireBase(Map<String, Object> messageHashMap) {
    this.databaseStore.storeMessage(messageHashMap, this.userAuthentication.getCurrentUserUid());
  }

  /**
   * Call to sync messages from FireStore to Room for the logged in user
   */
  public void syncMessagesFromFireStoreToRoom() {
    fireBaseApiManager.syncMessages(
        new DBValueListener<List<Map<String, Object>>>() {
          @Override
          public void onDataReceived(List<Map<String, Object>> data) {
            //                This code runs on the UI thread
            List<Message> messages = new ArrayList<>();
            for (Map<String, Object> object : data) {
              Timber.i(String.valueOf(object.get("mid")));
              Message newMessage =
                  new Message(
                      (long) object.get("mid"),
                      String.valueOf(object.get("msg")),
                      String.valueOf(object.get("sender")),
                      String.valueOf(object.get("receiver")),
                      (long) object.get("chatRoomId"),
                      (long) object.get("timestamp"));
              Timber.i(newMessage.toString());
              messages.add(newMessage);
            }
            addMessages(messages);
          }

          @Override
          public void onCancelled(Error error) {
          }
        });
  }

  /**
   * Fetch all chat room messages for the given ChatRoom through AsyncTask from Room
   */
  private static class FetchChatRoomMessagesAsyncTask
      extends AsyncTask<ChatRoom, Void, List<Message>> {

    private MessageDao mAsyncTaskDao;

    FetchChatRoomMessagesAsyncTask(MessageDao dao) {
      mAsyncTaskDao = dao;
    }

    @Override
    protected List<Message> doInBackground(ChatRoom... params) {
      return mAsyncTaskDao.getAllMessagesFromChatRoom(params[0].getId());
    }
  }

  /**
   * Fetch all chat room messages for the given ChatRoom through AsyncTask from Room
   */
  private static class ClearAllMessagesInChatRoomAsyncTask extends AsyncTask<ChatRoom, Void, Void> {

    private MessageDao mAsyncTaskDao;

    ClearAllMessagesInChatRoomAsyncTask(MessageDao dao) {
      mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(ChatRoom... params) {
      mAsyncTaskDao.clearChatRoomMessages(params[0].getId());
      return null;
    }
  }

  /**
   * Fetch all messages through AsyncTask from Room
   */
  private static class AddAllMessagesAsyncTask extends AsyncTask<List<Message>, Void, Void> {

    private MessageDao mAsyncTaskDao;

    AddAllMessagesAsyncTask(MessageDao dao) {
      mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(List<Message>... lists) {
      mAsyncTaskDao.insertAllMessages(lists[0]);
      return null;
    }
  }

  /**
   * Fetch all messages through AsyncTask from Room
   */
  private static class ClearAllMessagesAsyncTask extends AsyncTask<Void, Void, Void> {

    private MessageDao mAsyncTaskDao;

    ClearAllMessagesAsyncTask(MessageDao dao) {
      mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(Void... params) {
      mAsyncTaskDao.clear();
      return null;
    }
  }

  /**
   * Call to get favorite projects from Room through AsyncTask
   */
  private static class DeleteMessageAsyncTask extends AsyncTask<Message, Void, Message> {

    private MessageDao dao;

    DeleteMessageAsyncTask(MessageDao mDao) {
      dao = mDao;
    }

    @Override
    protected Message doInBackground(Message... params) {
      dao.deleteItem(params[0]);
      return params[0];
    }
  }

  /**
   * Fetch a specific project for the given Message ID through AsyncTask
   */
  private static class FetchMessageAsyncTask extends AsyncTask<Long, Void, Message> {

    private MessageDao mAsyncTaskDao;

    FetchMessageAsyncTask(MessageDao dao) {
      mAsyncTaskDao = dao;
    }

    @Override
    protected Message doInBackground(Long... params) {
      return mAsyncTaskDao.get(params[0]);
    }
  }

  /**
   * AsyncTask which makes insert operation thread safe and does not block the main thread for a
   * long time
   */
  private static class InsertAsyncTask extends AsyncTask<Message, Void, Message> {

    private MessageDao mAsyncTaskDao;

    InsertAsyncTask(MessageDao dao) {
      mAsyncTaskDao = dao;
    }

    @Override
    protected Message doInBackground(final Message... params) {
      mAsyncTaskDao.insert(params[0]);
      Timber.i("Inside AsyncTask to insert message in Room %s", params[0].getMsg());
      return mAsyncTaskDao.getLatestMessage(params[0].getChatRoomId());
    }
  }

  /**
   * AsyncTask which makes insert operation thread safe and does not block the main thread for a
   * long time
   */
  private static class UpdateAsyncTask extends AsyncTask<Message, Void, Void> {

    private MessageDao mAsyncTaskDao;

    UpdateAsyncTask(MessageDao dao) {
      mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(final Message... params) {
      mAsyncTaskDao.update(params[0]);
      return null;
    }
  }

  public interface OnMessageResponseReceivedListener {
    void onMessageResponseReceived(Message response);

    void onNoResponseReceived(Error error);
  }
}
