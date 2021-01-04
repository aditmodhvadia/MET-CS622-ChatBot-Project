package com.fazemeright.chatbotmetcs622.network.models

/**
 * To use when we want to have sync response from network manager.
 *
 * @param <T> type
</T> */
class NetCompoundRes<T> {
    var isSuccess = false
    var netResponse: NetResponse<T>? = null
    var netError: NetError? = null
}