package com.fazemeright.chatbotmetcs622.network;


import android.content.Context;

import androidx.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.network.handlers.NetworkCallback;
import com.fazemeright.chatbotmetcs622.network.handlers.NetworkWrapper;
import com.fazemeright.chatbotmetcs622.network.models.ChatBotError;
import com.fazemeright.chatbotmetcs622.network.models.NetCompoundRes;
import com.fazemeright.chatbotmetcs622.network.models.NetError;
import com.fazemeright.chatbotmetcs622.network.models.NetResponse;
import com.fazemeright.chatbotmetcs622.network.utils.CoreUtils;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import timber.log.Timber;

public class NetworkManager implements NetworkWrapper {

    private final static String TAG = NetworkManager.class.getSimpleName();
    private static final String CONTENT_TYPE = "application/json; charset=utf-8";
    private static NetworkManager networkManager = null;

    public static NetworkManager getInstance() {
        if (networkManager == null) {
            networkManager = new NetworkManager();
        }
        return networkManager;
    }

    /**
     * To get Http client
     *
     * @param requestTimeOut Network Request timeout in millisecond it's configurable from backend
     * @return OkHttpClient
     */
    public static OkHttpClient getHttpClient(int requestTimeOut) {
        //if set < 2 second then we put our default timeout

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(requestTimeOut, TimeUnit.SECONDS);
        builder.readTimeout(requestTimeOut, TimeUnit.SECONDS);
        builder.writeTimeout(requestTimeOut, TimeUnit.SECONDS);

        return builder.build();
    }

    /**
     * Initializing at the very first time
     * Set Request Timeout
     * Enabling network logging
     *
     * @param requestTimeOut Network Request timeout in millisecond it's configurable from backend
     * @param context
     */
    public void init(Context context, int requestTimeOut) {
        initSecureClient(context, requestTimeOut);
    }

    /**
     * To initialize network manager
     *
     * @param context        App context
     * @param requestTimeOut Network Request timeout in millisecond it's configurable from backend
     */
    private void initSecureClient(Context context, int requestTimeOut) {
        AndroidNetworking.initialize(context, getHttpClient(requestTimeOut));
    }

    @Override
    public <T> void makeGetRequest(Context context, String url, TypeToken typeToken, String tag,
                                   final NetworkCallback<T> networkCallback) {
        if (CoreUtils.isValidUrl(url)) {
            if (CoreUtils.isNetworkAvailable(context)) {
                printURLAndRequestParameters(url, null);
                AndroidNetworking.get(url)
                        .setTag(tag)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsParsed(typeToken, new ParsedRequestListener<T>() {
                            @Override
                            public void onResponse(T response) {
                                Timber.i("onResponse :: %s", CoreUtils.getStringFromObject(response));
                                NetResponse netResponse = new NetResponse();
                                netResponse.setResponse(response);
                                networkCallback.onSuccess(netResponse);
                            }

                            @Override
                            public void onError(ANError anError) {
                                Timber.e("onError :: %s", CoreUtils.getStringFromObject(anError));
                                networkCallback.onError(getNetError(anError, null));
                            }
                        });
            } else {
                networkCallback.onError(getNetErrorConnectivityError(getConnectivityError(context), null));
            }
        } else {
            networkCallback.onError(getInvalidUrlError(url));
        }
    }

    @Override
    public <T> void makePostRequest(Context context, String url, TypeToken typeToken,
                                    HashMap<String, String> hashMapHeader, String tag,
                                    final NetworkCallback<T> networkCallback) {
        if (CoreUtils.isValidUrl(url)) {
            if (CoreUtils.isNetworkAvailable(context)) {
                AndroidNetworking.post(url)
                        .setContentType("application/x-www-form-urlencoded") // custom ContentType
                        .setTag(tag)
                        .addHeaders(hashMapHeader)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsParsed(typeToken, new ParsedRequestListener<T>() {
                            @Override
                            public void onResponse(T response) {
                                NetResponse netResponse = new NetResponse();
                                netResponse.setResponse(response);
                                networkCallback.onSuccess(netResponse);
                            }

                            @Override
                            public void onError(ANError anError) {
                                networkCallback.onError(getNetError(anError, null));
                            }
                        });
            } else {
                networkCallback.onError(getNetErrorConnectivityError(getConnectivityError(context),
                        null));
            }
        } else {
            networkCallback.onError(getInvalidUrlError(url));
        }
    }


    @Override
    public <T> void makeGetRequestHeader(Context context, String url, TypeToken typeToken,
                                         HashMap<String, String> hashMapHeader, String tag,
                                         final NetworkCallback<T> networkCallback) {
        if (CoreUtils.isValidUrl(url)) {
            if (CoreUtils.isNetworkAvailable(context)) {
                printURLAndRequestParameters(url, null);
                AndroidNetworking.get(url)
                        .setTag(tag)
                        .addHeaders(hashMapHeader)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsParsed(typeToken, new ParsedRequestListener<T>() {
                            @Override
                            public void onResponse(T response) {
                                Timber.i("onResponse :: %s", CoreUtils.getStringFromObject(response));
                                NetResponse netResponse = new NetResponse();
                                netResponse.setResponse(response);
                                networkCallback.onSuccess(netResponse);
                            }

                            @Override
                            public void onError(ANError anError) {
                                Timber.e("onError :: %s", CoreUtils.getStringFromObject(anError));
                                networkCallback.onError(getNetError(anError, null));
                            }
                        });
            } else {
                networkCallback.onError(getNetErrorConnectivityError(getConnectivityError(context), null));
            }
        } else {
            networkCallback.onError(getInvalidUrlError(url));
        }
    }

    @Override
    public <T> void makePutRequestHeader(Context context, String url, final Object dataObject,
                                         TypeToken typeToken, HashMap<String, String> hashMapHeader,
                                         String tag, final NetworkCallback<T> networkCallback) {
        if (CoreUtils.isValidUrl(url)) {
            if (CoreUtils.isNetworkAvailable(context)) {
                printURLAndRequestParameters(url, dataObject);
                //.addBodyParameter(dataObject)
                //.addStringBody(NetworkUtility.getStringFromObject(dataObject))
                AndroidNetworking.put(url)
                        .addApplicationJsonBody(dataObject)
                        .addHeaders(hashMapHeader)
                        .setContentType(CONTENT_TYPE) // custom ContentType
                        .setTag(tag)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsParsed(typeToken, new ParsedRequestListener<T>() {
                            @Override
                            public void onResponse(T response) {
                                // LogUtils.getInstance().printLog(TAG, "onResponse :: "
                                // + CoreUtils.getStringFromObject(response));
                                NetResponse netResponse = new NetResponse();
                                netResponse.setResponse(response);
                                networkCallback.onSuccess(netResponse);
                            }

                            @Override
                            public void onError(ANError anError) {
                                //   LogUtils.getInstance().printLog(TAG, "onError :: "
                                // + CoreUtils.getStringFromObject(anError));
                                networkCallback.onError(getNetError(anError, dataObject));
                            }
                        });
            } else {
                networkCallback.onError(getNetErrorConnectivityError(getConnectivityError(context), dataObject));
            }
        } else {
            networkCallback.onError(getInvalidUrlError(url));
        }
    }


    @Override
    public <T> void makePostRequest(Context context, String url, final Object dataObject, TypeToken typeToken,
                                    String tag, final NetworkCallback<T> networkCallback) {
        if (CoreUtils.isValidUrl(url)) {
            if (CoreUtils.isNetworkAvailable(context)) {
                printURLAndRequestParameters(url, dataObject);
//.addBodyParameter(dataObject)
//.addStringBody(NetworkUtility.getStringFromObject(dataObject))
                AndroidNetworking.post(url)
                        .addApplicationJsonBody(dataObject)
//                        .addHeaders(hashMapHeader)
                        .setContentType(CONTENT_TYPE) // custom ContentType
                        .setTag(tag)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsParsed(typeToken, new ParsedRequestListener<T>() {
                            @Override
                            public void onResponse(T response) {
                                // LogUtils.getInstance().printLog(TAG, "onResponse :: "
                                // + CoreUtils.getStringFromObject(response));
                                NetResponse netResponse = new NetResponse();
                                netResponse.setResponse(response);
                                networkCallback.onSuccess(netResponse);
                            }

                            @Override
                            public void onError(ANError anError) {
                                //   LogUtils.getInstance().printLog(TAG, "onError :: "
                                // + CoreUtils.getStringFromObject(anError));
                                networkCallback.onError(getNetError(anError, dataObject));
                            }
                        });
            } else {
                networkCallback.onError(getNetErrorConnectivityError(getConnectivityError(context), dataObject));
            }
        } else {
            networkCallback.onError(getInvalidUrlError(url));
        }
    }

    @Override
    public <T> void makePostStringRequest(Context context, String url, String data, TypeToken typeToken,
                                          String tag, NetworkCallback<T> networkCallback) {

    }

    @Override
    public <T> NetCompoundRes<T> makePostRequestSync(Context context, String url, Object dataObject,
                                                     TypeToken typeToken, String tag) {
        return null;
    }

    @Override
    public <T> void makePostRequestHeader(Context context, String url, final Object dataObject,
                                          TypeToken typeToken, HashMap<String, String> hashMapHeader,
                                          String tag, final NetworkCallback<T> networkCallback) {

        if (CoreUtils.isValidUrl(url)) {
            if (CoreUtils.isNetworkAvailable(context)) {
                printURLAndRequestParameters(url, dataObject);
//.addBodyParameter(dataObject)
//.addStringBody(NetworkUtility.getStringFromObject(dataObject))
                AndroidNetworking.post(url)
                        .addApplicationJsonBody(dataObject)
                        .addHeaders(hashMapHeader)
                        .setContentType(CONTENT_TYPE) // custom ContentType
                        .setTag(tag)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsParsed(typeToken, new ParsedRequestListener<T>() {
                            @Override
                            public void onResponse(T response) {
                                // LogUtils.getInstance().printLog(TAG, "onResponse :: "
                                // + CoreUtils.getStringFromObject(response));
                                NetResponse netResponse = new NetResponse();
                                netResponse.setResponse(response);
                                networkCallback.onSuccess(netResponse);
                            }

                            @Override
                            public void onError(ANError anError) {
                                //   LogUtils.getInstance().printLog(TAG, "onError :: "
                                // + CoreUtils.getStringFromObject(anError));
                                networkCallback.onError(getNetError(anError, dataObject));
                            }
                        });
            } else {
                networkCallback.onError(getNetErrorConnectivityError(getConnectivityError(context), dataObject));
            }
        } else {
            networkCallback.onError(getInvalidUrlError(url));
        }
    }

    @Override
    public <T> void makeDeleteRequestHeader(Context context, String url, /*final Object dataObject,*/
                                            TypeToken typeToken, HashMap<String, String> hashMapHeader,
                                            String tag, final NetworkCallback<T> networkCallback) {

        if (CoreUtils.isValidUrl(url)) {
            if (CoreUtils.isNetworkAvailable(context)) {
//                printURLAndRequestParameters(url, dataObject);
//.addBodyParameter(dataObject)
//.addStringBody(NetworkUtility.getStringFromObject(dataObject))
                AndroidNetworking.delete(url)
//                        .addApplicationJsonBody(dataObject)
                        .addHeaders(hashMapHeader)
                        .setContentType(CONTENT_TYPE) // custom ContentType
                        .setTag(tag)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsParsed(typeToken, new ParsedRequestListener<T>() {
                            @Override
                            public void onResponse(T response) {
                                // LogUtils.getInstance().printLog(TAG, "onResponse :: "
                                // + CoreUtils.getStringFromObject(response));
                                NetResponse netResponse = new NetResponse();
                                netResponse.setResponse(response);
                                networkCallback.onSuccess(netResponse);
                            }

                            @Override
                            public void onError(ANError anError) {
                                //   LogUtils.getInstance().printLog(TAG, "onError :: "
                                // + CoreUtils.getStringFromObject(anError));
                                networkCallback.onError(getNetError(anError, null));
                            }
                        });
            } else {
                networkCallback.onError(getNetErrorConnectivityError(getConnectivityError(context), null));
            }
        } else {
            networkCallback.onError(getInvalidUrlError(url));
        }
    }

    @Override
    public void cancelRequest(String tag) {

    }

    /**
     * To use only for get id_token,access_token and refresh_token when login with Social
     *
     * @param context
     * @param url
     * @param dataObject
     * @param typeToken
     * @param hashMapHeader
     * @param tag
     * @param networkCallback
     * @param <T>
     */
    @Override
    public <T> void makeCustomPostForSocialRequest(Context context, String url, final String dataObject,
                                                   TypeToken<T> typeToken,
                                                   HashMap<String, String> hashMapHeader, String tag,
                                                   final NetworkCallback<T> networkCallback) {

        String stringURL = url.concat("?grant_type=authorization_code&redirect_uri=rosedaleapp://login&client_id=2idjeho7u7717nur0uhb6kmuhj&")
                .concat("code=").concat(dataObject).concat("&scope=email openid profile");

        if (CoreUtils.isValidUrl(url)) {
            if (CoreUtils.isNetworkAvailable(context)) {
                printURLAndRequestParameters(stringURL, dataObject);
                AndroidNetworking.post(stringURL)
                        .setContentType("application/x-www-form-urlencoded") // custom ContentType
                        .setTag(tag)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsParsed(typeToken, new ParsedRequestListener<T>() {
                            @Override
                            public void onResponse(T response) {
                                NetResponse netResponse = new NetResponse();
                                netResponse.setResponse(response);
                                networkCallback.onSuccess(netResponse);
                            }

                            @Override
                            public void onError(ANError anError) {
                                networkCallback.onError(getNetError(anError, dataObject));
                            }
                        });
            } else {
                networkCallback.onError(getNetErrorConnectivityError(getConnectivityError(context), dataObject));
            }
        } else {
            networkCallback.onError(getInvalidUrlError(url));
        }
    }

    /**
     * To create class for error from network/api
     *
     * @param anError
     * @param requestObject
     */
    private NetError getNetError(ANError anError, @Nullable Object requestObject) {
        NetError netError = new NetError(anError.getMessage());
        netError.setErrorBody(anError.getErrorBody());
        netError.setErrorCode(anError.getErrorCode());
        netError.setErrorDetail(anError.getErrorDetail());
        netError.setErrorLocalizeMessage(anError.getLocalizedMessage());
        netError.setApiRequest(requestObject);
        netError.setResponseErrorMessage(anError.getErrorBody());
        return netError;
    }

    /**
     * To create class for error from network/api
     *
     * @param anError
     * @param requestObject
     */
    private NetError getNetErrorConnectivityError(ANError anError, @Nullable Object requestObject) {
        NetError netError = new NetError(anError.getMessage());
        netError.setErrorBody(anError.getErrorBody());
        netError.setErrorCode(anError.getErrorCode());
        netError.setErrorDetail(anError.getErrorDetail());
        netError.setErrorLocalizeMessage(anError.getLocalizedMessage());
        netError.setApiRequest(requestObject);
        netError.setResponseErrorMessage(anError.getErrorBody());
        return netError;
    }

    /**
     * To get Connectivity Error
     *
     * @param context
     * @return
     */
    private ANError getConnectivityError(Context context) {
        ANError anError = new ANError(context.getString(R.string.no_internet_connection_available));
        anError.setErrorCode(ChatBotError.ChatBotErrorCodes.INTERNET_NOT_AVAILABLE);
        anError.setErrorBody(context.getString(R.string.no_internet_connection_available));
        return anError;
    }

    /**
     * To Print Request
     *
     * @param url
     * @param data
     */
    private void printURLAndRequestParameters(String url, Object data) {
        Timber.i("url :: %s", url);
        Timber.i("requestParameters :: %s",
                CoreUtils.getStringFromObject(data));
    }


    /**
     * To get Invalid Url Error
     *
     * @param url
     * @return
     */
    private NetError getInvalidUrlError(String url) {
        NetError anError = new NetError("Invalid url: " + url);
        anError.setErrorCode(ChatBotError.ChatBotErrorCodes.INVALID_URL);
        return anError;
    }


    private void enableAndroidNetworkingLogging(boolean enable) {
        if (enable) {
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        } else {
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.NONE);
        }
    }

}
