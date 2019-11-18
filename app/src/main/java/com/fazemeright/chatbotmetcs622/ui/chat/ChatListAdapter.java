package com.fazemeright.chatbotmetcs622.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.models.Message;

import java.util.ArrayList;

/**
 * RecyclerView Adapter to display Chat
 *
 * @see ChatActivity for use
 */
public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static final int POSITION_OF_ADDITION = 0;
    private static final int TYPE_SENT = 0;
    private static final int TYPE_RECEIVED = 1;
    private ArrayList<Message> messages;
    private Context context;


    ChatListAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == TYPE_SENT) { // for sent message layout
            view = LayoutInflater.from(context).inflate(R.layout.sender_message_display_view_item, viewGroup, false);
            return new SentViewHolder(view);

        } else { // for received message layout
            view = LayoutInflater.from(context).inflate(R.layout.receiver_message_display_view_item, viewGroup, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_SENT) {
            ((SentViewHolder) holder).bind(messages.get(position));
        } else {
            ((ReceivedViewHolder) holder).bind(messages.get(position));
        }
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
        return messages.size();
    }

    void addMessage(Message newMessage) {
        messages.add(POSITION_OF_ADDITION, newMessage);
        notifyItemInserted(POSITION_OF_ADDITION);
    }

    public interface ChatMessageInteractionListener {
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {

        TextView tvMsg, tvTimestamp;

        SentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMsg = itemView.findViewById(R.id.tvMsg);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }

        void bind(Message item) {
            tvMsg.setText(item.getMsg());
            tvTimestamp.setText(item.getFormattedTime());
        }
    }

    public class ReceivedViewHolder extends RecyclerView.ViewHolder {

        TextView tvMsg, tvTimestamp;

        ReceivedViewHolder(@NonNull View itemView) {
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
