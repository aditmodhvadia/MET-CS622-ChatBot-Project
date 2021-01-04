package com.fazemeright.chatbotmetcs622.models

import java.io.Serializable

/**
 * POJO to hold Chat room.
 */
class ChatRoom(
        /**
         * id of the chat room.
         */
        val id: Long,
        /**
         * Name of the chat room.
         */
        val name: String,
        /**
         * Id of resource file associated with the ChatRoom.
         */
        val logoId: Int) : Serializable {

    companion object {
        const val BRUTE_FORCE_ID = 0
        const val LUCENE_ID = 1
        const val MONGO_DB_ID = 2
        const val MY_SQL_ID = 3
        const val MONGO_DB = "MongoDB"
        const val MY_SQL = "MySQL"
        const val LUCENE = "Lucene"
        const val BRUTE_FORCE = "Brute Force"
    }
}