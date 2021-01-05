package com.fazemeright.chatbotmetcs622.ui.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fazemeright.chatbotmetcs622.R
import com.fazemeright.chatbotmetcs622.database.message.Message
import com.fazemeright.chatbotmetcs622.ui.chat.ChatListAdapter.MessageViewHolder
import java.util.*

/**
 * RecyclerView Adapter to display Chat.
 *
 * @see ChatActivity for use
 */
class ChatListAdapter constructor(private val context: Context) : ListAdapter<Message, MessageViewHolder>(MessageDiffCallback()) {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MessageViewHolder {
        val view: View
        val isViewSendMessageType = viewType == TYPE_SENT
        val isViewReceiveMessageType = viewType == TYPE_RECEIVED
        view = when {
            isViewSendMessageType -> {
                getInflatedLayout(viewGroup, R.layout.sender_message_display_view_item)
            }
            isViewReceiveMessageType -> { // for received message layout
                getInflatedLayout(viewGroup, R.layout.receiver_message_display_view_item)
            }
            else -> {
                // TODO: Update with something else. Currently will not result in this.
                getInflatedLayout(viewGroup, R.layout.receiver_message_display_view_item)
            }
        }
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * Get inflated layout for the view holder item.
     *
     * @param viewGroup   view group
     * @param layoutResId layout resource id
     * @return inflated layout view
     */
    private fun getInflatedLayout(viewGroup: ViewGroup, @LayoutRes layoutResId: Int): View {
        return LayoutInflater.from(context)
                .inflate(layoutResId, viewGroup, false)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position)!!.sender == Message.SENDER_USER) {
            TYPE_SENT
        } else {
            TYPE_RECEIVED
        }
    }

    /**
     * Call to remove all messages from the Data List and notify data set changed.
     */
    fun clearAllMessages() {
        submitList(ArrayList())
    }

    /**
     * Update list of messages.
     *
     * @param messages list of messages
     */
    fun updateList(messages: List<Message?>?) {
        submitList(messages)
    }

    class MessageViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvMsg: TextView = itemView.findViewById(R.id.tvMsg)
        var tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
        fun bind(item: Message?) {
            tvMsg.text = item!!.msg
            tvTimestamp.text = item.formattedTime
        }

    }

    /**
     * Diff calculator callback.
     */
    private class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.mid == newItem.mid
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        private const val TYPE_SENT = 0
        private const val TYPE_RECEIVED = 1
    }
}