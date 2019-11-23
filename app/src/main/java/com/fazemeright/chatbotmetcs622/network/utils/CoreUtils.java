package com.fazemeright.chatbotmetcs622.network.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.webkit.URLUtil;

import com.google.gson.Gson;


public class CoreUtils {

    /**
     * To check internet connection
     *
     * @param mContext App context
     * @return true if available else false
     */
    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String getStringFromObject(Object data) {
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    /**
     * Check whether URL is valid or not
     *
     * @param url url
     * @return true if valid else false
     */
    public static boolean isValidUrl(String url) {
        return URLUtil.isValidUrl(url);
    }

}
