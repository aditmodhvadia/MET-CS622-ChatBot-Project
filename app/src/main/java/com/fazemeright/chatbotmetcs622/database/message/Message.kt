package com.fazemeright.chatbotmetcs622.database.message;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.fazemeright.library.api.Storable;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * POJO for a message.
 */
@Entity(tableName = "my_messages_table")
public class Message implements Storable, Serializable {
  public static final String SENDER_USER = "User";
  /**
   * mid of message.
   */
  @PrimaryKey(autoGenerate = true)
  private final long mid;
  /**
   * text of message.
   */
  private final String msg;
  /**
   * sender of the message.
   */
  private final String sender;
  /**
   * receiver of the message.
   */
  private final String receiver;
  /**
   * mid of the chat room where message was sent.
   */
  private final long chatRoomId;
  /**
   * timestamp of the message.
   */
  private final long timestamp;

  /**
   * Constructor.
   *
   * @param mid        id
   * @param msg        message text
   * @param sender     sender name
   * @param receiver   receiver name
   * @param chatRoomId chat room id
   * @param timestamp  timestamp
   */
  public Message(
      long mid, String msg, String sender, String receiver, long chatRoomId, long timestamp) {
    this.mid = mid;
    this.msg = msg;
    this.sender = sender;
    this.receiver = receiver;
    this.chatRoomId = chatRoomId;
    this.timestamp = timestamp;
  }

  /**
   * Create a message object.
   *
   * @param msg        msg text
   * @param sender     sender name
   * @param receiver   receiver name
   * @param chatRoomId chat room id
   * @return Message object
   */
  public static Message newMessage(String msg, String sender, String receiver, long chatRoomId) {
    return new Message(0, msg, sender, receiver, chatRoomId, System.currentTimeMillis());
  }

  /**
   * Get Message object from Map.
   *
   * @param object Map
   * @return Message object
   */
  @NonNull
  public static Message fromMap(Map<String, Object> object) {
    return new Message(
        (long) object.get("mid"),
        String.valueOf(object.get("msg")),
        String.valueOf(object.get("sender")),
        String.valueOf(object.get("receiver")),
        (long) object.get("chatRoomId"),
        (long) object.get("timestamp"));
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

  /**
   * Get the formatted time.
   *
   * @return formatted date
   */
  public String getFormattedTime() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm a", Locale.getDefault());
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(timestamp);
    return simpleDateFormat.format(cal.getTime());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Message message = (Message) o;
    return mid == message.mid
        && chatRoomId == message.chatRoomId
        && timestamp == message.timestamp
        && Objects.equals(msg, message.msg)
        && Objects.equals(sender, message.sender)
        && Objects.equals(receiver, message.receiver);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mid, msg, sender, receiver, chatRoomId, timestamp);
  }

  @Override
  public @Nonnull
  Map<String, Object> getHashMap() {
    Map<String, Object> messageHashMap = new HashMap<>();
    messageHashMap.put("mid", getMid());
    messageHashMap.put("msg", getMsg());
    messageHashMap.put("sender", getSender());
    messageHashMap.put("receiver", getReceiver());
    messageHashMap.put("chatRoomId", getChatRoomId());
    messageHashMap.put("timestamp", getTimestamp());
    return messageHashMap;
  }

  @Override
  public long getId() {
    return getMid();
  }
}
