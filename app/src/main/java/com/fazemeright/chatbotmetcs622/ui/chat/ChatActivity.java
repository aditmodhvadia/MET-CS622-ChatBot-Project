package com.fazemeright.chatbotmetcs622.ui.chat;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.database.message.Message;
import com.fazemeright.chatbotmetcs622.models.ChatRoom;
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity;
import com.fazemeright.chatbotmetcs622.ui.landing.LandingActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import timber.log.Timber;

public class ChatActivity extends BaseActivity<ChatActivityViewModel>
    implements View.OnClickListener {

  private RecyclerView rvChatList;
  private ChatListAdapter adapter;
  private EditText etMsg;
  private ImageView ivSendMsg;
  private ChatRoom chatRoom;
  private ChipGroup dataFilterChipGroup;

  @Override
  protected Class<ChatActivityViewModel> getViewModelClass() {
    return ChatActivityViewModel.class;
  }

  @Override
  public void initViews() {

    etMsg = findViewById(R.id.etMsg);
    ivSendMsg = findViewById(R.id.ivSendMsg);
    rvChatList = findViewById(R.id.rvChatList);
    dataFilterChipGroup = findViewById(R.id.dataFilterChipGroup);

    if (getIntent() != null) {
      chatRoom = (ChatRoom) getIntent().getSerializableExtra(LandingActivity.SELECTED_CHAT_ROOM);
      setUpSupportActionBar();
    }

    setupFilterKeywords(getResources().getStringArray(R.array.query_sample_selection));

    adapter = new ChatListAdapter(context);

    setUpRecyclerView();

    viewModel.getMessagesForChatRoom(chatRoom).observe(this, messages -> {
      if (messages != null) {
        adapter.updateList(messages);
      } else {
        Timber.e("No messages found");
      }
    });

    viewModel.messageSent.observe(this, result -> {
      if (result.isSuccessful()) {
        etMsg.setText("");
        rvChatList.scrollToPosition(adapter.getItemCount());
      } else {
        // TODO: Show error to the user
        if (result.getException() != null) {
          Toast.makeText(context, result.getException().getMessage(), Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  /**
   * Set up the recyclerview.
   */
  private void setUpRecyclerView() {
    rvChatList.setAdapter(adapter);
    rvChatList.setLayoutManager(getLinearLayoutManager());
    rvChatList.setHasFixedSize(true);
    // Show user the most recent messages, hence scroll to the top
    rvChatList.scrollToPosition(adapter.getItemCount());
  }

  /**
   * Set up the support action bar.
   */
  private void setUpSupportActionBar() {
    if (getSupportActionBar() != null) {
      getSupportActionBar().setHomeButtonEnabled(true);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      if (chatRoom != null && chatRoom.getName() != null) {
        getSupportActionBar().setTitle(chatRoom.getName());
      }
    }
  }

  /**
   * Get LinearLayoutManager for the recyclerview.
   *
   * @return linear layout manager
   */
  private LinearLayoutManager getLinearLayoutManager() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
    linearLayoutManager.setReverseLayout(true);
    linearLayoutManager.setStackFromEnd(true);
    return linearLayoutManager;
  }

  /**
   * Use to setup chips for the given list of data filter for device usage.
   *
   * @param dataFilters given array of data filter
   */
  private void setupFilterKeywords(String[] dataFilters) {
    //        remove all views from ChipGroup if any
    dataFilterChipGroup.removeAllViews();
    if (dataFilters == null) {
      //      hide ChipGroup if list is empty
      dataFilterChipGroup.setVisibility(View.INVISIBLE);
    } else {
      for (final String dataFilter : dataFilters) {
        //                create new chip and apply attributes
        Chip chip = getChip(dataFilter);
        dataFilterChipGroup.addView(chip); // add chip to ChipGroup
        chip.setOnCheckedChangeListener(
            (buttonView, isChecked) -> {
              if (isChecked) {
                etMsg.requestFocus();
                etMsg.setText(buttonView.getText().toString());
                etMsg.setSelection(buttonView.getText().toString().length());
                showKeyBoard(etMsg);
              }
            });
      }
      dataFilterChipGroup.setVisibility(View.VISIBLE);
    }
  }

  @NotNull
  private Chip getChip(final String dataFilter) {
    return new Chip(Objects.requireNonNull(context)) {
      {
        setText(dataFilter);
        setClickable(true);
        setCloseIconVisible(false); // close icon not required
        setCheckable(true); // allow check changes, hence switch between chips
      }
    };
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    int itemId = item.getItemId();
    if (itemId == android.R.id.home) {
      onBackPressed();
    } else if (itemId == R.id.action_clear) {
      clearChatRoomMessagesClicked(chatRoom);
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * Call to clear all message for the given ChatRoom.
   *
   * @param chatRoom given ChatRoom
   */
  private void clearChatRoomMessagesClicked(ChatRoom chatRoom) {
    viewModel.clearAllChatRoomMessages(chatRoom);
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
   * User clicked send message. Show new message to user and pass it to repository.
   */
  private void sendMessageClicked() {
    String msg = etMsg.getText().toString().trim();
    if (TextUtils.isEmpty(msg)) {
      return;
    }
    Message newMessage =
        Message.newMessage(msg, Message.SENDER_USER, chatRoom.getName(), chatRoom.getId());
    viewModel.sendNewMessage(context, newMessage);
  }

  @Override
  public int getMenuId() {
    return R.menu.menu_chat;
  }
}
