package com.fazemeright.chatbotmetcs622.repositories;

import android.content.Context;
import android.os.AsyncTask;

import com.fazemeright.chatbotmetcs622.database.ChatBotDatabase;
import com.fazemeright.chatbotmetcs622.database.messages.Message;
import com.fazemeright.chatbotmetcs622.database.messages.MessageDao;
import com.fazemeright.chatbotmetcs622.models.ChatRoom;
import com.fazemeright.chatbotmetcs622.network.ApiManager;
import com.fazemeright.chatbotmetcs622.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import timber.log.Timber;


public class MessageRepository {

    private static MessageRepository repository;
    private ChatBotDatabase database;
    private ApiManager apiManager;

    private MessageRepository(ChatBotDatabase database, ApiManager apiManager) {
        this.database = database;
        this.apiManager = apiManager;
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
//                get instance of database
                ChatBotDatabase database = ChatBotDatabase.getInstance(context);
                ApiManager apiManager = ApiManager.getInstance();
                apiManager.init(NetworkManager.getInstance());
                repository = new MessageRepository(database, apiManager);
            }
        }
        return repository;
    }

    /**
     * Call to insert given project into database with thread safety
     *
     * @param newMessage given project
     */
    private void insertMessageInRoom(Message newMessage) {
//        insert into Room using AsyncTask
        Timber.i("Insert message in Room called%s", newMessage.getMsg());
        new InsertAsyncTask(database.messageDao()).execute(newMessage);
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
            return (ArrayList<Message>) new FetchChatRoomMessagesAsyncTask(database.messageDao()).execute(chatRoom).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Called when user sends a given new message in the ChatRoom
     * - Add new Message to Room
     * - Call API to fetch answer for new message
     * - Sync message with FireStore
     *
     * @param newMessage given new message
     */
    public void newMessageSent(Message newMessage) {
        insertMessageInRoom(newMessage);
//        TODO: Finish the remaining cart
    }

    public ArrayList<Message> getAllMessages() {
        return (ArrayList<Message>) database.messageDao().getAllMessages();
    }

    /**
     * Clear all given chat room messages
     * -    From Room
     * -    From FireStore
     *
     * @param chatRoom given chat room
     */
    public void clearAllChatRoomMessages(ChatRoom chatRoom) {
        clearAllChatRoomMessagesFromRoom(chatRoom);
    }

    private void clearAllChatRoomMessagesFromRoom(ChatRoom chatRoom) {
        new ClearAllMessagesAsyncTask(database.messageDao()).execute(chatRoom);
    }

    /**
     * Fetch all chat room  messages for the given ChatRoom through AsyncTask from Room
     */
    private static class FetchChatRoomMessagesAsyncTask extends AsyncTask<ChatRoom, Void, List<Message>> {

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
     * Fetch all chat room  messages for the given ChatRoom through AsyncTask from Room
     */
    private static class ClearAllMessagesAsyncTask extends AsyncTask<ChatRoom, Void, Void> {

        private MessageDao mAsyncTaskDao;

        ClearAllMessagesAsyncTask(MessageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ChatRoom... params) {
            mAsyncTaskDao.clearChatRoomMessages(params[0].getId());
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
     * AsyncTask which makes insert operation thread safe and does not block the main thread for a long time
     */
    private static class InsertAsyncTask extends AsyncTask<Message, Void, Void> {

        private MessageDao mAsyncTaskDao;

        InsertAsyncTask(MessageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Message... params) {
            mAsyncTaskDao.insert(params[0]);
            Timber.i("Inside AsyncTask to insert message in Room %s", params[0].getMsg());
            return null;
        }
    }

    /**
     * AsyncTask which makes insert operation thread safe and does not block the main thread for a long time
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
}
