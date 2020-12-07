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
import com.fazemeright.chatbotmetcs622.database.messages.Message;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * RecyclerView Adapter to display Chat
 *
 * @see ChatActivity for use
 */
public class ChatListAdapter extends ListAdapter<Message, ChatListAdapter.MessageViewHolder> {

  static final int MOST_RECENT_MSG_POSITION = 0;
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
      // TODO: Update with something else, like future message types. Currently will not result in this.
      view = getInflatedLayout(viewGroup, R.layout.receiver_message_display_view_item);
    }
    return new MessageViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
    holder.bind(getItem(position));
  }

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
   * Call to remove all messages from the Data List and notify data set changed
   */
  void clearAllMessages() {
    submitList(new ArrayList<>());
  }

  public void updateList(List<Message> messages) {
//    TODO: Call submit list in ListAdapter
    submitList(messages);
  }

  public interface ChatMessageInteractionListener {
  }

  /*public static class SentViewHolder extends MessageViewHolder {
      SentViewHolder(@NonNull View itemView) {
          super(itemView);
      }
  }

  public static class ReceivedViewHolder extends MessageViewHolder {

    ReceivedViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }*/

  public static class MessageViewHolder extends RecyclerView.ViewHolder {
    TextView tvMsg, tvTimestamp;

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
