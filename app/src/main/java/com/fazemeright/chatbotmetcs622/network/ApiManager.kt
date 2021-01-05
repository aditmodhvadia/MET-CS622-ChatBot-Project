package com.fazemeright.chatbotmetcs622.network

import android.content.Context
import com.fazemeright.chatbotmetcs622.database.message.Message
import com.fazemeright.chatbotmetcs622.models.ChatRoom
import com.fazemeright.chatbotmetcs622.network.handlers.NetworkCallback
import com.fazemeright.chatbotmetcs622.network.models.NetError
import com.fazemeright.chatbotmetcs622.network.models.NetResponse
import com.fazemeright.chatbotmetcs622.network.models.request.MessageQueryRequestModel
import com.fazemeright.chatbotmetcs622.network.models.response.QueryResponseMessage
import com.google.gson.reflect.TypeToken

class ApiManager {
    private var networkManager: NetworkManager? = null

    /**
     * Initialize base url,alias key and [NetworkWrapper] from application side, so that no need
     * to pass base url in every user related network call.
     *
     * @param networkManager network manager
     */
    fun init(networkManager: NetworkManager?) {
        this.networkManager = networkManager
    }

    /**
     * Call Query API to backend to fetch results for the given Message query.
     *
     * @param context         context
     * @param newMessage      given message query
     * @param networkCallback callback to listen to response
     */
    fun queryDatabase(
            context: Context,
            newMessage: Message,
            networkCallback: NetworkCallback<QueryResponseMessage>) {
        val url = BaseUrl.BASE_URL + BaseUrl.BASE_APP_NAME + getServerEndPoint(newMessage.chatRoomId.toInt())
        val messageQuery = MessageQueryRequestModel(newMessage.msg)
        val typeToken: TypeToken<QueryResponseMessage> = object : TypeToken<QueryResponseMessage>() {}
        networkManager?.makePostRequest(
                context,
                url,
                messageQuery,
                typeToken,
                "",
                object : NetworkCallback<QueryResponseMessage> {
                    override fun onSuccess(response: NetResponse<QueryResponseMessage>?) {
                        networkCallback.onSuccess(response)
                    }

                    override fun onError(error: NetError?) {
                        networkCallback.onError(error)
                    }
                })
    }

    private fun getServerEndPoint(chatRoomId: Int): String {
        val serverEndPoint: String
        serverEndPoint = when (chatRoomId) {
            ChatRoom.BRUTE_FORCE_ID -> DatabaseUrl.BRUTE_FORCE
            ChatRoom.LUCENE_ID -> DatabaseUrl.LUCENE
            ChatRoom.MONGO_DB_ID -> DatabaseUrl.MONGO_DB
            else -> DatabaseUrl.MY_SQL
        }
        return serverEndPoint
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

    /**
     * BaseURL model for all the BASE URL addresses.
     */
    object BaseUrl {
        const val BASE_APP_NAME = "/MET_CS622_ChatBot_Backend_war_exploded"
        var BASE_URL = "http://192.168.43.28:8080" //  Update the url as per your local ip address

        /**
         * Set the local IP address or the base url address for the server where the database back end
         * is hosted. Also include the port.
         *
         * @param hostAddress ip address
         */
        fun setLocalIp(hostAddress: String) {
            BASE_URL = hostAddress
        }
    }

    companion object {
        private val apiManager: ApiManager = ApiManager()

        /**
         * Get singleton instance.
         *
         * @return singleton instance
         */
        @JvmStatic
        val instance: ApiManager
            get() {
                synchronized(ApiManager::class.java) {
                    return apiManager
                }
            }
    }
}