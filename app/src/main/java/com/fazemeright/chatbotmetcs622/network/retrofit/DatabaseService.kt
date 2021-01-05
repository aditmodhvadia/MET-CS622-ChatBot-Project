package com.fazemeright.chatbotmetcs622.network.retrofit

import com.fazemeright.chatbotmetcs622.network.Constants.BASE_APP_NAME
import com.fazemeright.chatbotmetcs622.network.models.request.MessageQueryRequestModel
import com.fazemeright.chatbotmetcs622.network.models.response.QueryResponseMessage
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface DatabaseService {

    @POST("$BASE_APP_NAME/{dbName}")
    suspend fun queryDatabase(@Path("dbName") dbName: String, @Body query: MessageQueryRequestModel):
            QueryResponseMessage

}