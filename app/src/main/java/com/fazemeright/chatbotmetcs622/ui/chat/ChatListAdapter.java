package com.fazemeright.chatbotmetcs622.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.database.messages.Message;

import java.util.ArrayList;

/**
 * RecyclerView Adapter to display Chat
 *
 * @see ChatActivity for use
 */
public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  static final int MOST_RECENT_MSG_POSITION = 0;
  private static final int TYPE_SENT = 0;
  private static final int TYPE_RECEIVED = 1;
  private ArrayList<Message> messages;
  private final Context context;

  ChatListAdapter(ArrayList<Message> messages, Context context) {
    this.messages = messages;
    this.context = context;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
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

  private View getInflatedLayout(@NonNull ViewGroup viewGroup, @LayoutRes int layoutResId) {
    return LayoutInflater.from(context)
            .inflate(layoutResId, viewGroup, false);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      ((MessageViewHolder) holder).bind(messages.get(position));
  }

  @Override
  public int getItemViewType(int position) {
    if (messages.get(position).getSender().equals(Message.SENDER_USER)) {
      return TYPE_SENT;
    } else {
      return TYPE_RECEIVED;
    }
  }

  @Override
  public int getItemCount() {
    return messages != null ? messages.size() : 0;
  }

  /**
   * Call to add given new message to ArrayList at the bottom of the list and notify it was inserted
   *
   * @param newMessage given new message
   */
  void addMessage(Message newMessage) {
    if (messages == null) {
      messages = new ArrayList<>();
    }
    messages.add(MOST_RECENT_MSG_POSITION, newMessage);
    notifyItemInserted(MOST_RECENT_MSG_POSITION);
  }

  /** Call to remove all messages from the Data List and notify data set changed */
  void clearAllMessages() {
    int messagesSize = messages.size();
    messages.clear();
    notifyItemRangeRemoved(0, messagesSize);
  }

  public interface ChatMessageInteractionListener {}

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
}
