package com.example.network_library.retrofit

import com.example.network_library.DatabaseService
import com.example.network_library.models.request.MessageQueryRequestModel
import com.example.network_library.models.response.QueryResponseMessage
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitApiManager {
    private val retrofit = Retrofit.Builder().apply {
        baseUrl("http://localhost:8080")
        addConverterFactory(MoshiConverterFactory.create())
    }.build()

    private val databaseService: DatabaseService by lazy { retrofit.create(DatabaseService::class.java) }

    suspend fun queryDatabase(msg: String, dbName: String): QueryResponseMessage {
        return databaseService.queryDatabase(dbName, MessageQueryRequestModel(msg))
    }
}