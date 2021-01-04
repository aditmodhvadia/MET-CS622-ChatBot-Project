package com.fazemeright.chatbotmetcs622.ui.landing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fazemeright.chatbotmetcs622.R
import com.fazemeright.chatbotmetcs622.models.ChatRoom
import java.util.*

/**
 * RecyclerView Adapter to show Chat Rooms.
 *
 * @see LandingActivity for use
 */
class ChatSelectionListAdapter constructor(private val listener: ChatListInteractionListener) : ListAdapter<ChatRoom, ChatSelectionListAdapter.ViewHolder>(ChatRoomDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_room_display_view_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * Update list of chat rooms.
     *
     * @param dataList list of ChatRoom
     */
    fun submitDataList(dataList: ArrayList<ChatRoom>) {
        submitList(dataList)
    }

    /**
     * Chat List Item interaction listener.
     */
    interface ChatListInteractionListener {
        fun onChatRoomClicked(chatRoom: ChatRoom)
    }

    /**
     * Calculate the diff of list items.
     */
    internal class ChatRoomDiffCallBack : DiffUtil.ItemCallback<ChatRoom>() {
        override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvChatRoomName: TextView
        var ivChatRoom: ImageView

        /**
         * Bind model to view.
         *
         * @param item ChatRoom
         */
        fun bind(item: ChatRoom?) {
            tvChatRoomName.text = item!!.name
            ivChatRoom.setBackgroundResource(item.logoId)
            itemView.setOnClickListener { v: View? -> listener.onChatRoomClicked(getItem(adapterPosition)) }
        }

        /**
         * ViewHolder.
         *
         * @param itemView view for list item
         */
        init {
            tvChatRoomName = itemView.findViewById(R.id.tvChatRoomName)
            ivChatRoom = itemView.findViewById(R.id.ivChatRoom)
        }
    }
}