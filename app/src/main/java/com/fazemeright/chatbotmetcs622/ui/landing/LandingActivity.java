package com.fazemeright.chatbotmetcs622.ui.landing;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.models.ChatRoom;
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity;
import com.fazemeright.chatbotmetcs622.ui.chat.ChatActivity;
import com.fazemeright.chatbotmetcs622.ui.login.LoginActivity;

import java.util.ArrayList;

public class LandingActivity extends BaseActivity implements ChatSelectionListAdapter.ChatListInteractionListener {

    public static final String SELECTED_CHAT_ROOM = "chatRoomSelected";
    private RecyclerView rvChatRoomList;
    private ChatSelectionListAdapter adapter;

    @Override
    public void initViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.welcome_title) + " " + apiManager.getCurrentLoggedInUserEmail());
        }

        rvChatRoomList = findViewById(R.id.rvChatRoomList);
        rvChatRoomList.setHasFixedSize(true);
        rvChatRoomList.setLayoutManager(new LinearLayoutManager(mContext));
        rvChatRoomList.addItemDecoration(new DividerItemDecoration(rvChatRoomList.getContext(), LinearLayoutManager.VERTICAL));
        adapter = new ChatSelectionListAdapter(this);
        rvChatRoomList.setAdapter(adapter);

        adapter.submitDataList(getChatRoomList());
    }

    private ArrayList<ChatRoom> getChatRoomList() {
        ArrayList<ChatRoom> chatRooms = new ArrayList<>();
        chatRooms.add(new ChatRoom(0, "Brute Force"));
        chatRooms.add(new ChatRoom(1, "Lucene"));
        chatRooms.add(new ChatRoom(2, "MongoDB"));
        chatRooms.add(new ChatRoom(3, "MySQL"));
        return chatRooms;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logoutUser();
                openLoginActivity();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openLoginActivity() {
        startActivity(new Intent(LandingActivity.this, LoginActivity.class));
        finish();
    }

    private void logoutUser() {
        apiManager.logOutUser();
    }

    @Override
    public void setListeners() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_landing;
    }

    @Override
    public void onChatRoomClicked(ChatRoom chatRoom) {
//        TODO: Add chatroom object to intent before sending it as well
        Intent intent = new Intent(LandingActivity.this, ChatActivity.class);
        intent.putExtra(SELECTED_CHAT_ROOM, chatRoom);
        startActivity(intent);
    }
}