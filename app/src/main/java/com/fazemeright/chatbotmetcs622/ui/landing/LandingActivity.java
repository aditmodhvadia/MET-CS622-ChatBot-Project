package com.fazemeright.chatbotmetcs622.ui.landing;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.models.ChatRoom;
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity;
import com.fazemeright.chatbotmetcs622.ui.login.LoginActivity;

import java.util.ArrayList;

public class LandingActivity extends BaseActivity implements ChatListAdapter.ChatListInteractionListener {

    private RecyclerView rvChatList;
    private ChatListAdapter adapter;

    @Override
    public void initViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.welcome_title) + " " + apiManager.getCurrentLoggedInUserEmail());
        }

        rvChatList = findViewById(R.id.rvChatList);
        rvChatList.setHasFixedSize(true);
        rvChatList.setLayoutManager(new LinearLayoutManager(mContext));
        rvChatList.addItemDecoration(new DividerItemDecoration(rvChatList.getContext(), LinearLayoutManager.VERTICAL));
        adapter = new ChatListAdapter(this);
        rvChatList.setAdapter(adapter);

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
        Toast.makeText(mContext, chatRoom.getName() + " clicked", Toast.LENGTH_SHORT).show();
    }
}
