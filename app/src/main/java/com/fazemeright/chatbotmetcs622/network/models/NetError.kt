package com.fazemeright.chatbotmetcs622.network.models

import android.text.TextUtils
import com.fazemeright.chatbotmetcs622.network.models.ChatBotError.ChatBotErrorCodes
import org.json.JSONException
import org.json.JSONObject

/*
* Class for handling Network/API related errors
* */
class NetError(message: String?) : Exception(message) {
    /**
     * Set error body.
     *
     * @param errorBody error body
     */
    var errorBody = ""
        set(errorBody) {
            field = errorBody
        }
    var errorCode: Int = ChatBotErrorCodes.SOMETHING_WENT_WRONG

    /**
     * Set error detail.
     *
     * @param errorDetail error detail
     */
    var errorDetail = ""
        set(errorDetail) {
            field = errorDetail
        }

    /**
     * Set error localize message.
     *
     * @param errorLocalizeMessage message
     */
    var errorLocalizeMessage = ""
        set(errorLocalizeMessage) {
            field = errorLocalizeMessage
        }

    /**
     * Get api request.
     *
     * @return api request
     */
    var apiRequest: Any? = null

    /**
     * Set request name.
     *
     * @param requestName request name
     */
    var requestName = ""
        set(requestName) {
            field = requestName
        }
    var responseErrorMessage: String? = null
        private set

    fun setResponseErrorMessage(responseErrorMessage: String) {
        this.responseErrorMessage = parseJson(responseErrorMessage)
    }

    private fun parseJson(responseErrorMessage: String): String {
        var errorMessage = "Something went wrong!"
        if (!TextUtils.isEmpty(responseErrorMessage)) {
            errorMessage = try {
                val jsonObject = JSONObject(responseErrorMessage)
                if (jsonObject.has("responseMessage")) {
                    jsonObject.getString("responseMessage")
                } else {
                    return responseErrorMessage
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                return responseErrorMessage
            }
        }
        return errorMessage
    }

    /**
     * Constructor.
     *
     * @param message message
     */
    init {
        var message = message
        if (message == null) {
            message = "Getting null error message."
        }
        errorLocalizeMessage = message
        errorBody = message
        errorDetail = message
    }
}