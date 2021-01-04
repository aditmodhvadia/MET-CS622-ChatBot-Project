package com.fazemeright.chatbotmetcs622.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fazemeright.chatbotmetcs622.database.message.Message
import com.fazemeright.chatbotmetcs622.database.message.MessageDao

@Database(entities = [Message::class], version = 1, exportSchema = false)
abstract class ChatBotDatabase : RoomDatabase() {
    //    List all daos here
    abstract fun messageDao(): MessageDao

    companion object {
        private const val DATABASE_NAME = "chat_bot_database"
        private var INSTANCE: ChatBotDatabase? = null

        /**
         * Get singleton instance of the database.
         *
         * @param context application context
         * @return thread-safe database instance
         */
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context?): ChatBotDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context!!, ChatBotDatabase::class.java, DATABASE_NAME)
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return INSTANCE
        }
    }
}