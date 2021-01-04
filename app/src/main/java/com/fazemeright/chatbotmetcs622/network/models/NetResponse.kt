package com.fazemeright.chatbotmetcs622.network.models

import com.google.gson.annotations.SerializedName

/**
 * Class for handling generic response.
 */
class NetResponse<T> {
    @SerializedName("response")
    var response: T? = null
        private set

    fun setResponse(response: T) {
        this.response = response
    }
}