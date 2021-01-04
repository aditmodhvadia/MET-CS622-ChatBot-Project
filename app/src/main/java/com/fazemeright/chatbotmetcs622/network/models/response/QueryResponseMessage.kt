package com.fazemeright.chatbotmetcs622.network.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QueryResponseMessage(
        @SerializedName("data")
        @Expose
        var data: Data? = null)

data class Data(
        @SerializedName("responseMsg")
        @Expose
        var responseMsg: String? = null)

