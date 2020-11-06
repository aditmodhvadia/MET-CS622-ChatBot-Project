package com.fazemeright.chatbotmetcs622.database.messages;

import androidx.room.Dao;
import androidx.room.FtsOptions;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.fazemeright.chatbotmetcs622.database.BaseDao;

import java.util.List;

@Dao
public interface MessageDao extends BaseDao<Message> {

  @Query("SELECT * from my_messages_table WHERE mid = :key")
  Message get(long key);

  @Query("DELETE FROM my_messages_table")
  void clear();

  @Query("SELECT * FROM my_messages_table ORDER BY timestamp DESC")
  List<Message> getAllMessages();

  @Query("SELECT * FROM my_messages_table WHERE chatRoomId = :chatRoomId ORDER BY timestamp DESC")
  List<Message> getAllMessagesFromChatRoom(long chatRoomId);

  @Query("DELETE from my_messages_table WHERE chatRoomId = :chatRoomId")
  void clearChatRoomMessages(long chatRoomId);

  @Query(
      "SELECT * FROM my_messages_table WHERE chatRoomId = :chatRoomId ORDER BY timestamp DESC LIMIT 1")
  Message getLatestMessage(long chatRoomId);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAllMessages(List<Message> order);
}
