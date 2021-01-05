package com.fazemeright.chatbotmetcs622.network.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MessageQueryRequestModel(@field:Expose @field:SerializedName("query") var query: String)