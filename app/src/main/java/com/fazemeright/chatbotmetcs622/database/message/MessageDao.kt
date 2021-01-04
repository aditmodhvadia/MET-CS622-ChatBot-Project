package com.fazemeright.chatbotmetcs622.database.message

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fazemeright.chatbotmetcs622.database.BaseDao

@Dao
interface MessageDao : BaseDao<Message?> {
    @Query("SELECT * from my_messages_table WHERE mid = :key")
    operator fun get(key: Long): Message?

    @Query("DELETE FROM my_messages_table")
    fun clear()

    @get:Query("SELECT * FROM my_messages_table ORDER BY timestamp DESC")
    val allMessages: List<Message?>?

    @Query("SELECT * FROM my_messages_table WHERE chatRoomId = :chatRoomId ORDER BY timestamp DESC")
    fun getAllMessagesFromChatRoom(chatRoomId: Long): List<Message?>?

    @Query("SELECT * FROM my_messages_table WHERE chatRoomId = :chatRoomId ORDER BY timestamp DESC")
    fun getAllMessagesFromChatRoomLive(chatRoomId: Long): LiveData<List<Message?>?>?

    @Query("DELETE from my_messages_table WHERE chatRoomId = :chatRoomId")
    fun clearChatRoomMessages(chatRoomId: Long)

    @Query("SELECT * FROM my_messages_table WHERE"
            + " chatRoomId = :chatRoomId ORDER BY timestamp DESC LIMIT 1")
    fun getLatestMessage(chatRoomId: Long): Message?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMessages(messages: List<Message?>?)
}