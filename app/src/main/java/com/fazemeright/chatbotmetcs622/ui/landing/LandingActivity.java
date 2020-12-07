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
import com.fazemeright.chatbotmetcs622.ui.registration.RegistrationActivity;
import java.util.ArrayList;

public class LandingActivity extends BaseActivity<LandingActivityViewModel>
    implements ChatSelectionListAdapter.ChatListInteractionListener {

  public static final String SELECTED_CHAT_ROOM = "chatRoomSelected";
  private RecyclerView rvChatRoomList;
  private ChatSelectionListAdapter adapter;

  @Override
  protected Class<LandingActivityViewModel> getViewModelClass() {
    return LandingActivityViewModel.class;
  }

  @Override
  public void initViews() {
    if (getSupportActionBar() != null) {
      String firstName =
          viewModel.getUserName() != null ?
              viewModel.getUserName() : "Adit";
      getSupportActionBar().setTitle(getString(R.string.welcome_title) + " " + firstName);
    }

    rvChatRoomList = findViewById(R.id.rvChatRoomList);
    adapter = new ChatSelectionListAdapter(this);

    setUpRecyclerView();
  }

  /**
   * Set up the RecyclerView
   */
  private void setUpRecyclerView() {
    rvChatRoomList.setHasFixedSize(true);
    rvChatRoomList.setLayoutManager(new LinearLayoutManager(mContext));
    rvChatRoomList.addItemDecoration(
        new DividerItemDecoration(rvChatRoomList.getContext(), LinearLayoutManager.VERTICAL));
    rvChatRoomList.setAdapter(adapter);
    adapter.submitDataList(getChatRoomList());
  }

  /**
   * Get the static chat room list
   *
   * @return list of ChatRoom
   */
  private ArrayList<ChatRoom> getChatRoomList() {
    ArrayList<ChatRoom> chatRooms = new ArrayList<>();
    chatRooms.add(
        new ChatRoom(ChatRoom.BRUTE_FORCE_ID, ChatRoom.BRUTE_FORCE, R.drawable.brute_force_logo));
    chatRooms.add(new ChatRoom(ChatRoom.LUCENE_ID, ChatRoom.LUCENE, R.drawable.lucene_logo));
    chatRooms.add(new ChatRoom(ChatRoom.MONGO_DB_ID, ChatRoom.MONGO_DB, R.drawable.mongodb_logo));
    chatRooms.add(new ChatRoom(ChatRoom.MY_SQL_ID, ChatRoom.MY_SQL, R.drawable.mysql_logo));
    return chatRooms;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_landing, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.action_logout) {
      viewModel.logOutUser();
      openRegistrationActivity();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * Navigate to RegistrationActivity
   */
  private void openRegistrationActivity() {
    startActivity(new Intent(LandingActivity.this, RegistrationActivity.class));
    finish();
  }

  @Override
  public int getLayoutResId() {
    return R.layout.activity_landing;
  }

  @Override
  public void onChatRoomClicked(ChatRoom chatRoom) {
    Intent intent = new Intent(LandingActivity.this, ChatActivity.class);
    intent.putExtra(SELECTED_CHAT_ROOM, chatRoom);
    startActivity(intent);
  }
}
