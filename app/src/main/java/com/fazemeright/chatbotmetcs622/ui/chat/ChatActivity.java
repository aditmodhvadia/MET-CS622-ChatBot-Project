package com.fazemeright.chatbotmetcs622.ui.chat;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Objects;

public class ChatActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView rvChatList;
    private ChatListAdapter adapter;
    private ArrayList<Message> messages;
    private EditText etMsg;
    private ImageView ivSendMsg;
    private ChatRoom chatRoom;
    private ChipGroup dataFilterChipGroup;

    @Override
    public void initViews() {

        etMsg = findViewById(R.id.etMsg);
        ivSendMsg = findViewById(R.id.ivSendMsg);
        rvChatList = findViewById(R.id.rvChatList);
        dataFilterChipGroup = findViewById(R.id.dataFilterChipGroup);

        if (getIntent() != null) {
            chatRoom = (ChatRoom) getIntent().getSerializableExtra(LandingActivity.SELECTED_CHAT_ROOM);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(chatRoom.getName());
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        String[] dataFilters = getResources().getStringArray(R.array.query_sample_selection);
        setupFilterKeywords(dataFilters);

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

    /**
     * Use to setup chips for the given list of data filter for device usage
     *
     * @param dataFilters given array of data filter
     */
    private void setupFilterKeywords(String[] dataFilters) {
//        remove all views from ChipGroup if any
        dataFilterChipGroup.removeAllViews();
        if (dataFilters != null) {
            for (final String dataFilter :
                    dataFilters) {
//                create new chip and apply attributes
                Chip chip = new Chip(Objects.requireNonNull(mContext)) {{
                    setText(dataFilter);    // set text
                    setClickable(true);
                    setCloseIconVisible(false); // no need for close icon in our scenario
                    setCheckable(true); // set checkable to be true, hence allow check changes
                }};
                dataFilterChipGroup.addView(chip);  // add chip to ChipGroup
                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            etMsg.requestFocus();
                            etMsg.setText(buttonView.getText().toString());
                            etMsg.setSelection(buttonView.getText().toString().length());
                            showKeyBoard(etMsg);
                        }
                    }
                });

            }
//            show ChipGroup if list is not empty
            dataFilterChipGroup.setVisibility(View.VISIBLE);
        } else {
//            hide ChipGroup if list is empty
            dataFilterChipGroup.setVisibility(View.INVISIBLE);
        }
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
