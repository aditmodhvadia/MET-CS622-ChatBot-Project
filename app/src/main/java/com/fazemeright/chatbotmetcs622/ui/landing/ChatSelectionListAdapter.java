package com.fazemeright.chatbotmetcs622.ui.landing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.models.ChatRoom;
import java.util.ArrayList;

/**
 * RecyclerView Adapter to show Chat Rooms.
 *
 * @see LandingActivity for use
 */
public class ChatSelectionListAdapter
    extends ListAdapter<ChatRoom, ChatSelectionListAdapter.ViewHolder> {

  private final ChatListInteractionListener listener;

  protected ChatSelectionListAdapter(ChatListInteractionListener listener) {
    super(new ChatRoomDiffCallBack());
    this.listener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.chat_room_display_view_item, parent, false);

    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(getItem(position));
  }

  /**
   * Update list of chat rooms.
   *
   * @param dataList list of ChatRoom
   */
  public void submitDataList(ArrayList<ChatRoom> dataList) {
    submitList(dataList);
  }

  /**
   * Chat List Item interaction listener.
   */
  public interface ChatListInteractionListener {
    void onChatRoomClicked(ChatRoom chatRoom);
  }

  /**
   * Calculate the diff of list items.
   */
  static class ChatRoomDiffCallBack extends DiffUtil.ItemCallback<ChatRoom> {

    @Override
    public boolean areItemsTheSame(@NonNull ChatRoom oldItem, @NonNull ChatRoom newItem) {
      return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull ChatRoom oldItem, @NonNull ChatRoom newItem) {
      return oldItem.equals(newItem);
    }
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    TextView tvChatRoomName;
    ImageView ivChatRoom;

    /**
     * ViewHolder.
     *
     * @param itemView view for list item
     */
    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      tvChatRoomName = itemView.findViewById(R.id.tvChatRoomName);
      ivChatRoom = itemView.findViewById(R.id.ivChatRoom);
    }

    /**
     * Bind model to view.
     *
     * @param item ChatRoom
     */
    public void bind(ChatRoom item) {
      tvChatRoomName.setText(item.getName());

      ivChatRoom.setBackgroundResource(item.getLogoId());

      itemView.setOnClickListener(
          v -> listener.onChatRoomClicked(getItem(getAdapterPosition())));
    }
  }
}
