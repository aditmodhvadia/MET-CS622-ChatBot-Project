package com.fazemeright.chatbotmetcs622.network;

import android.content.Context;

import com.fazemeright.chatbotmetcs622.network.handlers.NetworkCallback;
import com.fazemeright.chatbotmetcs622.network.handlers.NetworkWrapper;
import com.fazemeright.chatbotmetcs622.network.models.NetError;
import com.fazemeright.chatbotmetcs622.network.models.NetResponse;
import com.fazemeright.chatbotmetcs622.network.models.response.QueryResponseMessage;
import com.google.gson.reflect.TypeToken;

public class ApiManager {

    private static ApiManager apiManager = null;
    private NetworkManager networkManager;

    public static ApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

    /**
     * Initialize base url,alias key and {@link NetworkWrapper} from application side,
     * so that no need to pass base url in every user related network call.
     *
     * @param networkManager
     */
    public void init(NetworkManager networkManager) {
        this.networkManager = networkManager;

    }

    public void hiWorld(Context context, final NetworkCallback<QueryResponseMessage> networkCallback) {
        String url = BaseUrl.BASE_URL.concat(BaseUrl.BASE_APP_NAME).concat(BaseUrl.BASE_HI);

        TypeToken<QueryResponseMessage> typeToken = new TypeToken<QueryResponseMessage>() {
        };
        networkManager.makeGetRequest(context, url, typeToken, "", new NetworkCallback<QueryResponseMessage>() {
            @Override
            public void onSuccess(NetResponse<QueryResponseMessage> response) {
                networkCallback.onSuccess(response);
            }

            @Override
            public void onError(NetError error) {
                networkCallback.onError(error);
            }
        });
    }

    /**
     * QueryModel module Api sub url
     */
    class QueryModel {
        final static String MONGO_DB = "/mongodb";
        final static String LUCENE = "/lucene";
        final static String MY_SQL = "/mysql";
        final static String BRUTE_FORCE = "/bruteforce";

    }

    class BaseUrl {
        final static String BASE_URL = "http://192.168.0.24:8080";
        final static String BASE_APP_NAME = "/MET_CS622_ChatBot_Backend_war_exploded";
        final static String BASE_HI = "/hi";

    }
}
