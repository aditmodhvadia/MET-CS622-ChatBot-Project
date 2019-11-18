package com.fazemeright.chatbotmetcs622.ui.chat;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.models.ChatRoom;
import com.fazemeright.chatbotmetcs622.models.Message;
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity;
import com.fazemeright.chatbotmetcs622.ui.landing.LandingActivity;

import java.util.ArrayList;
import java.util.Collections;

public class ChatActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView rvChatList;
    private ChatListAdapter adapter;
    private ArrayList<Message> messages;
    private EditText etMsg;
    private ImageView ivSendMsg;
    private ChatRoom chatRoom;

    @Override
    public void initViews() {

        etMsg = findViewById(R.id.etMsg);
        ivSendMsg = findViewById(R.id.ivSendMsg);
        rvChatList = findViewById(R.id.rvChatList);

        if (getIntent() != null) {
            chatRoom = (ChatRoom) getIntent().getSerializableExtra(LandingActivity.SELECTED_CHAT_ROOM);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(chatRoom.getName());
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        setupDummyData();
        rvChatList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvChatList.setLayoutManager(linearLayoutManager);

        adapter = new ChatListAdapter(getMessages(), mContext);

        rvChatList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDummyData() {
        messages = new ArrayList<>();
        messages.add(new Message(0, "Hi", Message.SENDER_USER, chatRoom.getName(), chatRoom.getId(), System.currentTimeMillis() - 2500));
        messages.add(new Message(1, "Hi how are you", chatRoom.getName(), Message.SENDER_USER, chatRoom.getId(), System.currentTimeMillis() - 1500));
        messages.add(new Message(2, "I am fine thank you, What about you", Message.SENDER_USER, chatRoom.getName(), chatRoom.getId(), System.currentTimeMillis() - 500));
        messages.add(new Message(3, "I'm great, thank you for asking", chatRoom.getName(), Message.SENDER_USER, chatRoom.getId(), System.currentTimeMillis()));
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
        ivSendMsg.setOnClickListener(this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_chat;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivSendMsg) {
            sendMessageClicked();
        }
    }

    private void sendMessageClicked() {
        String msg = etMsg.getText().toString().trim();
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        etMsg.setText("");
        adapter.addMessage(Message.newMessage(msg, Message.SENDER_USER, ChatRoom.MONGODB, 0));
        rvChatList.scrollToPosition(ChatListAdapter.POSITION_OF_ADDITION);
    }
}
