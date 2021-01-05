package com.fazemeright.chatbotmetcs622.network.retrofit

import com.fazemeright.chatbotmetcs622.database.message.Message
import com.fazemeright.chatbotmetcs622.models.ChatRoom
import com.fazemeright.chatbotmetcs622.network.models.request.MessageQueryRequestModel
import com.fazemeright.chatbotmetcs622.network.models.response.QueryResponseMessage
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitApiManager {
    private val retrofit = Retrofit.Builder().apply {
        baseUrl("http://localhost:8080")
        addConverterFactory(MoshiConverterFactory.create())
    }.build()

    private val databaseService: DatabaseService by lazy { retrofit.create(DatabaseService::class.java) }

    suspend fun queryDatabase(message: Message): QueryResponseMessage {
        return getServerEndPoint(message.chatRoomId.toInt()).let {
            databaseService.queryDatabase(it, MessageQueryRequestModel(message.msg))
        }
    }


    private fun getServerEndPoint(chatRoomId: Int): String {
        return when (chatRoomId) {
            ChatRoom.BRUTE_FORCE_ID -> DatabaseUrl.BRUTE_FORCE
            ChatRoom.LUCENE_ID -> DatabaseUrl.LUCENE
            ChatRoom.MONGO_DB_ID -> DatabaseUrl.MONGO_DB
            else -> DatabaseUrl.MY_SQL
        }
    }

    /**
     * DatabaseUrl module Api sub url.
     */
    internal object DatabaseUrl {
        const val MONGO_DB = "/mongodb"
        const val LUCENE = "/lucene"
        const val MY_SQL = "/mysql"
        const val BRUTE_FORCE = "/bruteforce"
    }
}