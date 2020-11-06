package com.fazemeright.chatbotmetcs622.database.messages;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** POJO for a message */
@Entity(tableName = "my_messages_table")
public class Message implements Serializable {
  public static final String SENDER_USER = "User";
  /** mid of message */
  @PrimaryKey(autoGenerate = true)
  private long mid;
  /** text of message */
  private String msg;
  /** sender of the message */
  private String sender;
  /** receiver of the message */
  private String receiver;
  /** mid of the chat room where message was sent */
  private long chatRoomId;
  /** timestamp of the message */
  private long timestamp;

  public Message(
      long mid, String msg, String sender, String receiver, long chatRoomId, long timestamp) {
    this.mid = mid;
    this.msg = msg;
    this.sender = sender;
    this.receiver = receiver;
    this.chatRoomId = chatRoomId;
    this.timestamp = timestamp;
  }

  public static Message newMessage(String msg, String sender, String receiver, long chatRoomId) {
    return new Message(0, msg, sender, receiver, chatRoomId, System.currentTimeMillis());
  }

  public static Map<String, Object> getHashMap(Message message) {
    Map<String, Object> messageHashMap = new HashMap<>();
    messageHashMap.put("mid", message.getMid());
    messageHashMap.put("msg", message.getMsg());
    messageHashMap.put("sender", message.getSender());
    messageHashMap.put("receiver", message.getReceiver());
    messageHashMap.put("chatRoomId", message.getChatRoomId());
    messageHashMap.put("timestamp", message.getTimestamp());
    return messageHashMap;
  }

  public long getMid() {
    return mid;
  }

  public String getMsg() {
    return msg;
  }

  public String getSender() {
    return sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public long getChatRoomId() {
    return chatRoomId;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public String getFormattedTime() {
    String pattern = "MM/dd HH:mm a";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(timestamp);
    return simpleDateFormat.format(cal.getTime());
  }
}
