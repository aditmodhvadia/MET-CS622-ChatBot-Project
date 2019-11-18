package com.fazemeright.chatbotmetcs622.ui.chat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.models.ChatRoom;
import com.fazemeright.chatbotmetcs622.models.Message;
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.Collections;

public class ChatActivity extends BaseActivity {

    private RecyclerView rvChatList;
    private ChatListAdapter adapter;
    private ArrayList<Message> messages;

    @Override
    public void initViews() {
//        TODO: Set chat room name as title

        setupDummyData();
        rvChatList = findViewById(R.id.rvChatList);
        rvChatList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvChatList.setLayoutManager(linearLayoutManager);

        adapter = new ChatListAdapter(getMessages(), mContext);

        rvChatList.setAdapter(adapter);
    }

    private void setupDummyData() {
        messages = new ArrayList<>();
        messages.add(new Message(0, "Hi", Message.SENDER_USER, ChatRoom.MONGODB, 0, System.currentTimeMillis() - 2500));
        messages.add(new Message(1, "Hi how are you", ChatRoom.MONGODB, Message.SENDER_USER, 0, System.currentTimeMillis() - 1500));
        messages.add(new Message(2, "I am fine thank you, What about you", Message.SENDER_USER, ChatRoom.MONGODB, 0, System.currentTimeMillis() - 500));
        messages.add(new Message(3, "I'm great, thank you for asking", ChatRoom.MONGODB, Message.SENDER_USER, 0, System.currentTimeMillis()));
        Collections.reverse(messages);
    }

    /**
     * Get dummy data for messages
     *
     * @return dummy data
     */
    private ArrayList<Message> getMessages() {
        return messages;
    }

    @Override
    public void setListeners() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_chat;
    }
}
