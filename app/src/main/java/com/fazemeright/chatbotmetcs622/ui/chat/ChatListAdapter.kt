package com.fazemeright.chatbotmetcs622.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.database.message.Message;
import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView Adapter to display Chat.
 *
 * @see ChatActivity for use
 */
public class ChatListAdapter extends ListAdapter<Message, ChatListAdapter.MessageViewHolder> {

  private static final int TYPE_SENT = 0;
  private static final int TYPE_RECEIVED = 1;
  private final Context context;

  protected ChatListAdapter(Context context) {
    super(new MessageDiffCallback());
    this.context = context;
  }


  @NonNull
  @Override
  public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    View view;
    boolean isViewSendMessageType = viewType == TYPE_SENT;
    boolean isViewReceiveMessageType = viewType == TYPE_RECEIVED;
    if (isViewSendMessageType) {
      view = getInflatedLayout(viewGroup, R.layout.sender_message_display_view_item);
    } else if (isViewReceiveMessageType) { // for received message layout
      view = getInflatedLayout(viewGroup, R.layout.receiver_message_display_view_item);
    } else {
      // TODO: Update with something else. Currently will not result in this.
      view = getInflatedLayout(viewGroup, R.layout.receiver_message_display_view_item);
    }
    return new MessageViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
    holder.bind(getItem(position));
  }

  /**
   * Get inflated layout for the view holder item.
   *
   * @param viewGroup   view group
   * @param layoutResId layout resource id
   * @return inflated layout view
   */
  private View getInflatedLayout(@NonNull ViewGroup viewGroup, @LayoutRes int layoutResId) {
    return LayoutInflater.from(context)
        .inflate(layoutResId, viewGroup, false);
  }

  @Override
  public int getItemViewType(int position) {
    if (getItem(position).getSender().equals(Message.SENDER_USER)) {
      return TYPE_SENT;
    } else {
      return TYPE_RECEIVED;
    }
  }

  /**
   * Call to remove all messages from the Data List and notify data set changed.
   */
  void clearAllMessages() {
    submitList(new ArrayList<>());
  }

  /**
   * Update list of messages.
   *
   * @param messages list of messages
   */
  public void updateList(List<Message> messages) {
    submitList(messages);
  }

  public static class MessageViewHolder extends RecyclerView.ViewHolder {
    TextView tvMsg;
    TextView tvTimestamp;

    MessageViewHolder(@NonNull View itemView) {
      super(itemView);
      tvMsg = itemView.findViewById(R.id.tvMsg);
      tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
    }

    void bind(Message item) {
      tvMsg.setText(item.getMsg());
      tvTimestamp.setText(item.getFormattedTime());
    }
  }

  /**
   * Diff calculator callback.
   */
  private static class MessageDiffCallback extends DiffUtil.ItemCallback<Message> {
    @Override
    public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
      return oldItem.getMid() == newItem.getMid();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
      return oldItem.equals(newItem);
    }
  }
}
