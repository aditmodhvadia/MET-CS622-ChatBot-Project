package com.fazemeright.chatbotmetcs622.ui.chat;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.database.messages.Message;
import com.fazemeright.chatbotmetcs622.models.ChatRoom;
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository;
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity;
import com.fazemeright.chatbotmetcs622.ui.landing.LandingActivity;

import java.util.ArrayList;

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

        rvChatList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvChatList.setLayoutManager(linearLayoutManager);

        ArrayList<Message> messages = messageRepository.getMessagesForChatRoom(chatRoom);
        adapter = new ChatListAdapter(messages, mContext);

        rvChatList.setAdapter(adapter);
//        Show user the most recent messages, hence scroll to the top
        rvChatList.scrollToPosition(ChatListAdapter.MOST_RECENT_MSG_POSITION);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_clear:
                clearChatRoomMessagesClicked(chatRoom);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Call to clear all message for the given ChatRoom
     *
     * @param chatRoom given ChatRoom
     */
    private void clearChatRoomMessagesClicked(ChatRoom chatRoom) {
        messageRepository.clearAllChatRoomMessages(chatRoom);
        adapter.clearAllMessages();
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

    /**
     * User clicked send message. Show new message to user and pass it to repository
     */
    private void sendMessageClicked() {
        String msg = etMsg.getText().toString().trim();
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        etMsg.setText("");
        Message newMessage = Message.newMessage(msg, Message.SENDER_USER, chatRoom.getName(), chatRoom.getId());
        addMessageToAdapter(newMessage);
//        send new message to repository
        messageRepository.newMessageSent(mContext, newMessage, new MessageRepository.OnMessageResponseReceivedListener() {
            @Override
            public void onMessageResponseReceived(Message response) {
                addMessageToAdapter(response);
            }

            @Override
            public void onNoResponseReceived(Error error) {
                Toast.makeText(mContext, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                TODO: Show error to the user
            }
        });
    }

    /**
     * Call to add given new message to the Adapter and display it to the user and scroll to it
     *
     * @param newMessage given new message to be displayed
     */
    private void addMessageToAdapter(Message newMessage) {
        adapter.addMessage(newMessage);
        rvChatList.scrollToPosition(ChatListAdapter.MOST_RECENT_MSG_POSITION);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
