package com.fazemeright.chatbotmetcs622.models;

import java.io.Serializable;

/**
 * POJO to hold Chat room
 */
public class ChatRoom implements Serializable {
    public static final String MONGODB = "MongoDB";
    /**
     * is of the chat room
     */
    private int id;
    /**
     * Name of the chat room
     */
    private String name;

    public ChatRoom(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
