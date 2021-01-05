package com.fazemeright.chatbotmetcs622.network.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.webkit.URLUtil
import com.google.gson.Gson

object CoreUtils {
    /**
     * To check internet connection.
     *
     * @param context App context
     * @return true if available else false
     */
    @JvmStatic
    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo: NetworkInfo? = null
        networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    @JvmStatic
    fun getStringFromObject(data: Any?): String {
        val gson = Gson()
        return gson.toJson(data)
    }

    /**
     * Check whether URL is valid or not.
     *
     * @param url url
     * @return true if valid else false
     */
    @JvmStatic
    fun isValidUrl(url: String?): Boolean {
        return URLUtil.isValidUrl(url)
    }
}