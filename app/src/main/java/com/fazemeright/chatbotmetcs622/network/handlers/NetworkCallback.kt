package com.fazemeright.chatbotmetcs622.network.handlers

import com.fazemeright.chatbotmetcs622.network.models.NetError
import com.fazemeright.chatbotmetcs622.network.models.NetResponse

/*
* Interface for handling API response
* */
interface NetworkCallback<T> {
    /**
     * Interface method called on success of api call.
     *
     * @param response [NetResponse]
     */
    fun onSuccess(response: NetResponse<T>?)

    /**
     * Interface method called on api/network failure.
     *
     * @param error Error obj with error detail
     */
    fun onError(error: NetError?)
}