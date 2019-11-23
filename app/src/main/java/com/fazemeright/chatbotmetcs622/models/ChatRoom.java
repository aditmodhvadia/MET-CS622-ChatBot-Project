package com.fazemeright.chatbotmetcs622.models;

import java.io.Serializable;

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

    public ChatRoom(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
