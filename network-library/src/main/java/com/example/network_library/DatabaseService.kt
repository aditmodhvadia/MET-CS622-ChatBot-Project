package com.example.network_library

import com.example.network_library.Constants.BASE_APP_NAME
import com.example.network_library.models.request.MessageQueryRequestModel
import com.example.network_library.models.response.QueryResponseMessage
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface DatabaseService {

    @POST("$BASE_APP_NAME/{dbName}")
    suspend fun queryDatabase(@Path("dbName") dbName: String, @Body query: MessageQueryRequestModel):
            QueryResponseMessage

}