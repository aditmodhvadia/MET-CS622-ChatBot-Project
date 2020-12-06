package com.fazemeright.chatbotmetcs622.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.fazemeright.chatbotmetcs622.database.messages.Message;
import com.fazemeright.chatbotmetcs622.database.messages.MessageDao;

@Database(
    entities = {Message.class},
    version = 1,
    exportSchema = false)
public abstract class ChatBotDatabase extends RoomDatabase {

  //    singleton instance of Database
  private static ChatBotDatabase INSTANCE;

  public static synchronized ChatBotDatabase getInstance(Context context) {
    if (INSTANCE == null) {
      INSTANCE =
          Room.databaseBuilder(context, ChatBotDatabase.class, "chat_bot_database")
              // Wipes and rebuilds instead of migrating if no Migration object.
              .fallbackToDestructiveMigration()
              .build();
    }

    return INSTANCE;
  }

  //    List all daos here
  public abstract MessageDao messageDao();
}
