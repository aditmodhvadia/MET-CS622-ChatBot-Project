package com.fazemeright.chatbotmetcs622.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

/**
 * POJO for a message
 */
public class Message {
    public static final String SENDER_USER = "User";
    /**
     * id of message
     */
    private long id;
    /**
     * text of message
     */
    private String msg;
    /**
     * sender of the message
     */
    private String sender;
    /**
     * receiver of the message
     */
    private String receiver;
    /**
     * id of the chat room where message was sent
     */
    private int chatRoomId;
    /**
     * timestamp of the message
     */
    private long timestamp;

    public Message(long id, String msg, String sender, String receiver, int chatRoomId, long timestamp) {
        this.id = id;
        this.msg = msg;
        this.sender = sender;
        this.receiver = receiver;
        this.chatRoomId = chatRoomId;
        this.timestamp = timestamp;
    }

    public static Message newMessage(String msg, String sender, String receiver, int chatRoomId) {
        return new Message(new Random().nextInt(), msg, sender, receiver, chatRoomId, System.currentTimeMillis());
    }

    public long getId() {
        return id;
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

    public int getChatRoomId() {
        return chatRoomId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getFormattedTime() {
        String pattern = "HH:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return simpleDateFormat.format(cal.getTime());
    }
}
