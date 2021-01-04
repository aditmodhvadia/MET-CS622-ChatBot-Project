package com.fazemeright.chatbotmetcs622.database.message

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fazemeright.library.api.Storable
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "my_messages_table")
class Message(
        /**
         * mid of message.
         */
        @PrimaryKey(autoGenerate = true) val mid: Long,
        /**
         * text of message.
         */
        val msg: String,
        /**
         * sender of the message.
         */
        val sender: String,
        /**
         * receiver of the message.
         */
        val receiver: String,
        /**
         * mid of the chat room where message was sent.
         */
        val chatRoomId: Long,
        /**
         * timestamp of the message.
         */
        val timestamp: Long) : Storable, Serializable {

    /**
     * Get the formatted time.
     *
     * @return formatted date
     */
    val formattedTime: String
        get() {
            val simpleDateFormat = SimpleDateFormat("MM/dd HH:mm a", Locale.getDefault())
            val cal = Calendar.getInstance()
            cal.timeInMillis = timestamp
            return simpleDateFormat.format(cal.time)
        }

    override fun getHashMap(): Map<String, Any> {
        val messageHashMap: MutableMap<String, Any> = HashMap()
        messageHashMap["mid"] = mid
        messageHashMap["msg"] = msg
        messageHashMap["sender"] = sender
        messageHashMap["receiver"] = receiver
        messageHashMap["chatRoomId"] = chatRoomId
        messageHashMap["timestamp"] = timestamp
        return messageHashMap
    }

    override fun getId(): Long {
        return mid
    }

    companion object {
        const val SENDER_USER = "User"

        /**
         * Create a message object.
         *
         * @param msg        msg text
         * @param sender     sender name
         * @param receiver   receiver name
         * @param chatRoomId chat room id
         * @return Message object
         */
        @JvmStatic
        fun newMessage(msg: String, sender: String, receiver: String, chatRoomId: Long): Message {
            return Message(0, msg, sender, receiver, chatRoomId, System.currentTimeMillis())
        }

        /**
         * Get Message object from Map.
         *
         * @param `object` Map
         * @return Message object
         */
        @JvmStatic
        fun fromMap(map: Map<String?, Any>): Message {
            return Message(
                    map["mid"] as Long, map["msg"].toString(), map["sender"].toString(), map["receiver"].toString(),
                    map["chatRoomId"] as Long,
                    map["timestamp"] as Long)
        }
    }
}