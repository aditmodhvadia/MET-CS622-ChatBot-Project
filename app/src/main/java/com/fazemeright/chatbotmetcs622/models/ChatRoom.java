package com.fazemeright.chatbotmetcs622.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * POJO to hold Chat room
 */
public class ChatRoom implements Serializable {
  public static final int BRUTE_FORCE_ID = 0;
  public static final int LUCENE_ID = 1;
  public static final int MONGO_DB_ID = 2;
  public static final int MY_SQL_ID = 3;

  public static final String MONGO_DB = "MongoDB";
  public static final String MY_SQL = "MySQL";
  public static final String LUCENE = "Lucene";
  public static final String BRUTE_FORCE = "Brute Force";
  /**
   * is of the chat room
   */
  private long id;
  /**
   * Name of the chat room
   */
  private String name;
  /**
   * Id of resource file associated with the ChatRoom
   */
  private int logoId;

  public ChatRoom(int id, String name, int logoId) {
    this.id = id;
    this.name = name;
    this.logoId = logoId;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getLogoId() {
    return logoId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatRoom chatRoom = (ChatRoom) o;
    return id == chatRoom.id && logoId == chatRoom.logoId && name.equals(chatRoom.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, logoId);
  }
}
