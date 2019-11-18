package com.fazemeright.chatbotmetcs622.models;

/**
 * POJO to hold Chat room
 */
public class ChatRoom {
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
