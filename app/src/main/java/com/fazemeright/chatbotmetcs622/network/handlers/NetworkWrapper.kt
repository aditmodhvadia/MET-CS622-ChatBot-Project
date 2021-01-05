package com.fazemeright.chatbotmetcs622.network.handlers

import android.content.Context
import com.fazemeright.chatbotmetcs622.network.models.NetCompoundRes
import com.google.gson.reflect.TypeToken
import java.util.*

/*
* Interface for calling API
* */
interface NetworkWrapper {
    /**
     * API GET method Request.
     *
     * @param context         App context
     * @param url             Request URL
     * @param typeToken       [TypeToken] of the expected parsed object
     * @param tag             request tag
     * @param networkCallback Network callback
     */
    fun <T> makeGetRequest(
            context: Context?,
            url: String?,
            typeToken: TypeToken<T>?,
            tag: String?,
            networkCallback: NetworkCallback<T>?)

    /**
     * API POST method Request.
     *
     * @param context         App context
     * @param url             Request URL
     * @param typeToken       [TypeToken] of the expected parsed object
     * @param tag             request tag
     * @param networkCallback Network callback
     */
    fun <T> makePostRequest(
            context: Context?,
            url: String?,
            typeToken: TypeToken<T>?,
            hashMapHeader: HashMap<String?, String?>?,
            tag: String?,
            networkCallback: NetworkCallback<T>?)

    /**
     * API POST method Request.
     *
     * @param context         App context
     * @param url             Request URL
     * @param dataObject      Request body
     * @param typeToken       [TypeToken] of the expected parsed object
     * @param tag             request tag
     * @param networkCallback Network callback
     */
    fun <T> makePostRequest(
            context: Context?,
            url: String?,
            dataObject: Any?,
            typeToken: TypeToken<T>?,
            tag: String?,
            networkCallback: NetworkCallback<T>?)

    /**
     * API POST method Request.
     *
     * @param context         App context
     * @param url             Request URL
     * @param data            Request body
     * @param typeToken       [TypeToken] of the expected parsed object
     * @param tag             request tag
     * @param networkCallback Network callback
     */
    fun <T> makePostStringRequest(
            context: Context?,
            url: String?,
            data: String?,
            typeToken: TypeToken<T>?,
            tag: String?,
            networkCallback: NetworkCallback<T>?)

    /**
     * API sync POST method Request. Expect a response/error in [NetCompoundRes].
     *
     * @param context    App context
     * @param url        Request URL
     * @param dataObject Request body
     * @param typeToken  [TypeToken] of the expected parsed object
     * @param tag        request tag
     * @return Compound response ( It contains success / error ).
     */
    fun <T> makePostRequestSync(
            context: Context?, url: String?, dataObject: Any?, typeToken: TypeToken<T>?, tag: String?): NetCompoundRes<T>?

    /**
     * API POST method Request with header.
     *
     * @param context         App context
     * @param url             Request URL
     * @param dataObject      Request body
     * @param typeToken       [TypeToken] of the expected parsed object
     * @param hashMapHeader   HashMap of request header
     * @param tag             request tag
     * @param networkCallback Network callback
     */
    fun <T> makePostRequestHeader(
            context: Context?,
            url: String?,
            dataObject: Any?,
            typeToken: TypeToken<T>?,
            hashMapHeader: HashMap<String?, String?>?,
            tag: String?,
            networkCallback: NetworkCallback<T>?)

    /**
     * API POST method Request with header.
     *
     * @param context         App context
     * @param url             Request URL
     * @param typeToken       [TypeToken] of the expected parsed object
     * @param hashMapHeader   HashMap of request header
     * @param tag             request tag
     * @param networkCallback Network callback
     */
    fun <T> makeDeleteRequestHeader(
            context: Context?,
            url: String? /*, Object dataObject*/,
            typeToken: TypeToken<T>?,
            hashMapHeader: HashMap<String?, String?>?,
            tag: String?,
            networkCallback: NetworkCallback<T>?)

    /**
     * API Get method Request with header.
     *
     * @param context         App context
     * @param url             Request URL
     * @param typeToken       [TypeToken] of the expected parsed object
     * @param hashMapHeader   HashMap of request header
     * @param tag             request tag
     * @param networkCallback Network callback
     */
    fun <T> makeGetRequestHeader(
            context: Context?,
            url: String?,
            typeToken: TypeToken<T>?,
            hashMapHeader: HashMap<String?, String?>?,
            tag: String?,
            networkCallback: NetworkCallback<T>?)

    /**
     * API PUT Method Request with header.
     *
     * @param context         context
     * @param url             url
     * @param dataObject      object
     * @param typeToken       type
     * @param hashMapHeader   header
     * @param tag             tag
     * @param networkCallback callback
     * @param <T>             type
    </T> */
    fun <T> makePutRequestHeader(
            context: Context?,
            url: String?,
            dataObject: Any?,
            typeToken: TypeToken<T>?,
            hashMapHeader: HashMap<String?, String?>?,
            tag: String?,
            networkCallback: NetworkCallback<T>?)

    /**
     * Cancel api request by tag.
     *
     * @param tag request tag
     */
    fun cancelRequest(tag: String?)
    fun <T> makeCustomPostForSocialRequest(
            context: Context?,
            url: String?,
            dataObject: String?,
            typeToken: TypeToken<T>?,
            hashMapAuthenticate: HashMap<String?, String?>?,
            s: String?,
            networkCallback: NetworkCallback<T>?)
}