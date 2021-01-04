package com.fazemeright.chatbotmetcs622.network

import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.androidnetworking.interfaces.ParsedRequestListener
import com.fazemeright.chatbotmetcs622.R
import com.fazemeright.chatbotmetcs622.network.handlers.NetworkCallback
import com.fazemeright.chatbotmetcs622.network.handlers.NetworkWrapper
import com.fazemeright.chatbotmetcs622.network.models.ChatBotError
import com.fazemeright.chatbotmetcs622.network.models.NetCompoundRes
import com.fazemeright.chatbotmetcs622.network.models.NetError
import com.fazemeright.chatbotmetcs622.network.models.NetResponse
import com.fazemeright.chatbotmetcs622.network.utils.CoreUtils.getStringFromObject
import com.fazemeright.chatbotmetcs622.network.utils.CoreUtils.isNetworkAvailable
import com.fazemeright.chatbotmetcs622.network.utils.CoreUtils.isValidUrl
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class NetworkManager : NetworkWrapper {
    /**
     * Initializing at the very first time Set Request Timeout Enabling network logging.
     *
     * @param requestTimeOut Network Request timeout in millisecond it's configurable from backend
     * @param context        context
     */
    fun init(context: Context, requestTimeOut: Int) {
        initSecureClient(context, requestTimeOut)
    }

    /**
     * To initialize network manager.
     *
     * @param context        App context
     * @param requestTimeOut Network Request timeout in millisecond it's configurable from backend
     */
    private fun initSecureClient(context: Context, requestTimeOut: Int) {
        AndroidNetworking.initialize(context, getHttpClient(requestTimeOut))
    }

    override fun <T> makeGetRequest(
            context: Context?,
            url: String?,
            typeToken: TypeToken<T>?,
            tag: String?,
            networkCallback: NetworkCallback<T>?) {
        if (isValidUrl(url)) {
            if (isNetworkAvailable(context!!)) {
                printUrlAndRequestParameters(url, null)
                AndroidNetworking.get(url)
                        .setTag(tag)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsParsed(
                                typeToken,
                                object : ParsedRequestListener<T> {
                                    override fun onResponse(response: T) {
                                        Timber.i("onResponse :: %s", getStringFromObject(response))
                                        val netResponse = NetResponse<T>()
                                        netResponse.setResponse(response)
                                        networkCallback!!.onSuccess(netResponse)
                                    }

                                    override fun onError(anError: ANError) {
                                        Timber.e("onError :: %s", getStringFromObject(anError))
                                        networkCallback!!.onError(getNetError(anError, null))
                                    }
                                })
            } else {
                networkCallback!!.onError(getNetErrorConnectivityError(getConnectivityError(context), null))
            }
        } else {
            networkCallback!!.onError(getInvalidUrlError(url))
        }
    }

    override fun <T> makePostRequest(
            context: Context?,
            url: String?,
            dataObject: Any?,
            typeToken: TypeToken<T>?,
            tag: String?,
            networkCallback: NetworkCallback<T>?) {
        if (isValidUrl(url)) {
            if (isNetworkAvailable(context!!)) {
                printUrlAndRequestParameters(url, dataObject)
                // .addBodyParameter(dataObject)
                // .addStringBody(NetworkUtility.getStringFromObject(dataObject))
                AndroidNetworking.post(url)
                        .addApplicationJsonBody(dataObject) //                        .addHeaders(hashMapHeader)
                        .setContentType(CONTENT_TYPE) // custom ContentType
                        .setTag(tag)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsParsed(
                                typeToken,
                                object : ParsedRequestListener<T> {
                                    override fun onResponse(response: T) {
                                        // LogUtils.getInstance().printLog(TAG, "onResponse :: "
                                        // + CoreUtils.getStringFromObject(response));
                                        val netResponse = NetResponse<T>()
                                        netResponse.setResponse(response)
                                        networkCallback!!.onSuccess(netResponse)
                                    }

                                    override fun onError(anError: ANError) {
                                        //   LogUtils.getInstance().printLog(TAG, "onError :: "
                                        // + CoreUtils.getStringFromObject(anError));
                                        networkCallback!!.onError(getNetError(anError, dataObject))
                                    }
                                })
            } else {
                networkCallback!!.onError(
                        getNetErrorConnectivityError(getConnectivityError(context), dataObject))
            }
        } else {
            networkCallback!!.onError(getInvalidUrlError(url))
        }
    }

    override fun <T> makePostRequest(
            context: Context?,
            url: String?,
            typeToken: TypeToken<T>?,
            hashMapHeader: HashMap<String?, String?>?,
            tag: String?,
            networkCallback: NetworkCallback<T>?) {
        if (isValidUrl(url)) {
            if (isNetworkAvailable(context!!)) {
                AndroidNetworking.post(url)
                        .setContentType("application/x-www-form-urlencoded") // custom ContentType
                        .setTag(tag)
                        .addHeaders(hashMapHeader)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsParsed(
                                typeToken,
                                object : ParsedRequestListener<T> {
                                    override fun onResponse(response: T) {
                                        val netResponse = NetResponse<T>()
                                        netResponse.setResponse(response)
                                        networkCallback!!.onSuccess(netResponse)
                                    }

                                    override fun onError(anError: ANError) {
                                        networkCallback!!.onError(getNetError(anError, null))
                                    }
                                })
            } else {
                networkCallback!!.onError(getNetErrorConnectivityError(getConnectivityError(context), null))
            }
        } else {
            networkCallback!!.onError(getInvalidUrlError(url))
        }
    }

    override fun <T> makeGetRequestHeader(
            context: Context?,
            url: String?,
            typeToken: TypeToken<T>?,
            hashMapHeader: HashMap<String?, String?>?,
            tag: String?,
            networkCallback: NetworkCallback<T>?) {
        if (isValidUrl(url)) {
            if (isNetworkAvailable(context!!)) {
                printUrlAndRequestParameters(url, null)
                AndroidNetworking.get(url)
                        .setTag(tag)
                        .addHeaders(hashMapHeader)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsParsed(
                                typeToken,
                                object : ParsedRequestListener<T> {
                                    override fun onResponse(response: T) {
                                        Timber.i("onResponse :: %s", getStringFromObject(response))
                                        val netResponse = NetResponse<T>()
                                        netResponse.setResponse(response)
                                        networkCallback!!.onSuccess(netResponse)
                                    }

                                    override fun onError(anError: ANError) {
                                        Timber.e("onError :: %s", getStringFromObject(anError))
                                        networkCallback!!.onError(getNetError(anError, null))
                                    }
                                })
            } else {
                networkCallback!!.onError(getNetErrorConnectivityError(getConnectivityError(context), null))
            }
        } else {
            networkCallback!!.onError(getInvalidUrlError(url))
        }
    }

    override fun <T> makePutRequestHeader(
            context: Context?,
            url: String?,
            dataObject: Any?,
            typeToken: TypeToken<T>?,
            hashMapHeader: HashMap<String?, String?>?,
            tag: String?,
            networkCallback: NetworkCallback<T>?) {
        if (isValidUrl(url)) {
            if (isNetworkAvailable(context!!)) {
                printUrlAndRequestParameters(url, dataObject)
                // .addBodyParameter(dataObject)
                // .addStringBody(NetworkUtility.getStringFromObject(dataObject))
                AndroidNetworking.put(url)
                        .addApplicationJsonBody(dataObject)
                        .addHeaders(hashMapHeader)
                        .setContentType(CONTENT_TYPE) // custom ContentType
                        .setTag(tag)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsParsed(
                                typeToken,
                                object : ParsedRequestListener<T> {
                                    override fun onResponse(response: T) {
                                        // LogUtils.getInstance().printLog(TAG, "onResponse :: "
                                        // + CoreUtils.getStringFromObject(response));
                                        val netResponse = NetResponse<T>()
                                        netResponse.setResponse(response)
                                        networkCallback!!.onSuccess(netResponse)
                                    }

                                    override fun onError(anError: ANError) {
                                        //   LogUtils.getInstance().printLog(TAG, "onError :: "
                                        // + CoreUtils.getStringFromObject(anError));
                                        networkCallback!!.onError(getNetError(anError, dataObject))
                                    }
                                })
            } else {
                networkCallback!!.onError(
                        getNetErrorConnectivityError(getConnectivityError(context), dataObject))
            }
        } else {
            networkCallback!!.onError(getInvalidUrlError(url))
        }
    }

    override fun <T> makePostStringRequest(
            context: Context?,
            url: String?,
            data: String?,
            typeToken: TypeToken<T>?,
            tag: String?,
            networkCallback: NetworkCallback<T>?) {
    }

    override fun <T> makePostRequestSync(
            context: Context?, url: String?, dataObject: Any?, typeToken: TypeToken<T>?, tag: String?): NetCompoundRes<T>? {
        return null
    }

    override fun <T> makePostRequestHeader(
            context: Context?,
            url: String?,
            dataObject: Any?,
            typeToken: TypeToken<T>?,
            hashMapHeader: HashMap<String?, String?>?,
            tag: String?,
            networkCallback: NetworkCallback<T>?) {
        if (isValidUrl(url)) {
            if (isNetworkAvailable(context!!)) {
                printUrlAndRequestParameters(url, dataObject)
                // .addBodyParameter(dataObject)
                // .addStringBody(NetworkUtility.getStringFromObject(dataObject))
                AndroidNetworking.post(url)
                        .addApplicationJsonBody(dataObject)
                        .addHeaders(hashMapHeader)
                        .setContentType(CONTENT_TYPE) // custom ContentType
                        .setTag(tag)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsParsed(
                                typeToken,
                                object : ParsedRequestListener<T> {
                                    override fun onResponse(response: T) {
                                        // LogUtils.getInstance().printLog(TAG, "onResponse :: "
                                        // + CoreUtils.getStringFromObject(response));
                                        val netResponse = NetResponse<T>()
                                        netResponse.setResponse(response)
                                        networkCallback!!.onSuccess(netResponse)
                                    }

                                    override fun onError(anError: ANError) {
                                        //   LogUtils.getInstance().printLog(TAG, "onError :: "
                                        // + CoreUtils.getStringFromObject(anError));
                                        networkCallback!!.onError(getNetError(anError, dataObject))
                                    }
                                })
            } else {
                networkCallback!!.onError(
                        getNetErrorConnectivityError(getConnectivityError(context), dataObject))
            }
        } else {
            networkCallback!!.onError(getInvalidUrlError(url))
        }
    }

    override fun <T> makeDeleteRequestHeader(
            context: Context?,
            url: String?,  /*final Object dataObject,*/
            typeToken: TypeToken<T>?,
            hashMapHeader: HashMap<String?, String?>?,
            tag: String?,
            networkCallback: NetworkCallback<T>?) {
        if (isValidUrl(url)) {
            if (isNetworkAvailable(context!!)) {
                //                printURLAndRequestParameters(url, dataObject);
                // .addBodyParameter(dataObject)
                // .addStringBody(NetworkUtility.getStringFromObject(dataObject))
                AndroidNetworking.delete(url) //                        .addApplicationJsonBody(dataObject)
                        .addHeaders(hashMapHeader)
                        .setContentType(CONTENT_TYPE) // custom ContentType
                        .setTag(tag)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsParsed(
                                typeToken,
                                object : ParsedRequestListener<T> {
                                    override fun onResponse(response: T) {
                                        // LogUtils.getInstance().printLog(TAG, "onResponse :: "
                                        // + CoreUtils.getStringFromObject(response));
                                        val netResponse = NetResponse<T>()
                                        netResponse.setResponse(response)
                                        networkCallback!!.onSuccess(netResponse)
                                    }

                                    override fun onError(anError: ANError) {
                                        //   LogUtils.getInstance().printLog(TAG, "onError :: "
                                        // + CoreUtils.getStringFromObject(anError));
                                        networkCallback!!.onError(getNetError(anError, null))
                                    }
                                })
            } else {
                networkCallback!!.onError(getNetErrorConnectivityError(getConnectivityError(context), null))
            }
        } else {
            networkCallback!!.onError(getInvalidUrlError(url))
        }
    }

    override fun cancelRequest(tag: String?) {}

    /**
     * To use only for get id_token,access_token and refresh_token when login with Social.
     *
     * @param context         context
     * @param url             endpoint
     * @param dataObject      request data
     * @param typeToken       response type
     * @param hashMapHeader   header
     * @param tag             request tag
     * @param networkCallback network callback
     * @param <T>             Type
    </T> */
    override fun <T> makeCustomPostForSocialRequest(
            context: Context?,
            url: String?,
            dataObject: String?,
            typeToken: TypeToken<T>?,
            hashMapHeader: HashMap<String?, String?>?,
            tag: String?,
            networkCallback: NetworkCallback<T>?) {
        val stringUrl = url + "code=" + dataObject + "&scope=email openid profile"
        if (isValidUrl(url)) {
            if (isNetworkAvailable(context!!)) {
                printUrlAndRequestParameters(stringUrl, dataObject)
                AndroidNetworking.post(stringUrl)
                        .setContentType("application/x-www-form-urlencoded") // custom ContentType
                        .setTag(tag)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsParsed(
                                typeToken,
                                object : ParsedRequestListener<T> {
                                    override fun onResponse(response: T) {
                                        val netResponse = NetResponse<T>()
                                        netResponse.setResponse(response)
                                        networkCallback!!.onSuccess(netResponse)
                                    }

                                    override fun onError(anError: ANError) {
                                        networkCallback!!.onError(getNetError(anError, dataObject))
                                    }
                                })
            } else {
                networkCallback!!.onError(
                        getNetErrorConnectivityError(getConnectivityError(context), dataObject))
            }
        } else {
            networkCallback!!.onError(getInvalidUrlError(url))
        }
    }

    /**
     * To create class for error from network/api.
     *
     * @param anError       network error
     * @param requestObject request object
     */
    private fun getNetError(anError: ANError, requestObject: Any?): NetError {
        val netError = NetError(anError.message)
        netError.errorBody = anError.errorBody
        netError.errorCode = anError.errorCode
        netError.errorDetail = anError.errorDetail
        netError.errorLocalizeMessage = anError.localizedMessage
        netError.apiRequest = requestObject
        netError.setResponseErrorMessage(anError.errorBody)
        return netError
    }

    /**
     * To create class for error from network/api.
     *
     * @param anError       network error
     * @param requestObject request object
     */
    private fun getNetErrorConnectivityError(anError: ANError, requestObject: Any?): NetError {
        val netError = NetError(anError.message)
        netError.errorBody = anError.errorBody
        netError.errorCode = anError.errorCode
        netError.errorDetail = anError.errorDetail
        netError.errorLocalizeMessage = anError.localizedMessage
        netError.apiRequest = requestObject
        netError.setResponseErrorMessage(anError.errorBody)
        return netError
    }

    /**
     * To get Connectivity Error.
     *
     * @param context context
     * @return network error
     */
    private fun getConnectivityError(context: Context?): ANError {
        val anError = ANError(context!!.getString(R.string.no_internet_connection_available))
        anError.errorCode = ChatBotError.ChatBotErrorCodes.INTERNET_NOT_AVAILABLE
        anError.errorBody = context.getString(R.string.no_internet_connection_available)
        return anError
    }

    /**
     * To Print Request.
     *
     * @param url  endpoint
     * @param data data
     */
    private fun printUrlAndRequestParameters(url: String?, data: Any?) {
        Timber.i("url :: %s", url)
        Timber.i("requestParameters :: %s", getStringFromObject(data))
    }

    /**
     * To get Invalid Url Error.
     *
     * @param url url
     * @return network error if occurred
     */
    private fun getInvalidUrlError(url: String?): NetError {
        val anError = NetError("Invalid url: $url")
        anError.errorCode = ChatBotError.ChatBotErrorCodes.INVALID_URL
        return anError
    }

    private fun enableAndroidNetworkingLogging(enable: Boolean) {
        if (enable) {
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY)
        } else {
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.NONE)
        }
    }

    companion object {
        private val TAG = NetworkManager::class.java.simpleName
        private const val CONTENT_TYPE = "application/json; charset=utf-8"
        private var networkManager: NetworkManager? = null

        /**
         * Get singleton instance.
         *
         * @return instance
         */
        @JvmStatic
        val instance: NetworkManager?
            get() {
                if (networkManager == null) {
                    networkManager = NetworkManager()
                }
                return networkManager
            }

        /**
         * To get Http client.
         *
         * @param requestTimeOut Network Request timeout in millisecond it's configurable from backend
         * @return OkHttpClient
         */
        fun getHttpClient(requestTimeOut: Int): OkHttpClient {
            // if set < 2 second then we put our default timeout
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(requestTimeOut.toLong(), TimeUnit.SECONDS)
            builder.readTimeout(requestTimeOut.toLong(), TimeUnit.SECONDS)
            builder.writeTimeout(requestTimeOut.toLong(), TimeUnit.SECONDS)
            return builder.build()
        }
    }
}