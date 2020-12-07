package com.fazemeright.chatbotmetcs622.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.fazemeright.chatbotmetcs622.database.message.Message;
import com.fazemeright.chatbotmetcs622.database.message.MessageDao;

@Database(
    entities = {Message.class},
    version = 1,
    exportSchema = false)
public abstract class ChatBotDatabase extends RoomDatabase {

  public static final String DATABASE_NAME = "chat_bot_database";
  //    singleton instance of Database
  private static ChatBotDatabase INSTANCE;

  /**
   * Get singleton instance of the database
   *
   * @param context application cotext
   * @return thread-safe database instance
   */
  public static synchronized ChatBotDatabase getInstance(Context context) {
    if (INSTANCE == null) {
      INSTANCE =
          Room.databaseBuilder(context, ChatBotDatabase.class, DATABASE_NAME)
              // Wipes and rebuilds instead of migrating if no Migration object.
              .fallbackToDestructiveMigration()
              .build();
    }
    return INSTANCE;
  }

  //    List all daos here


  public abstract MessageDao messageDao();
}
