package com.fazemeright.chatbotmetcs622.network.handlers;


import com.fazemeright.chatbotmetcs622.network.models.NetError;
import com.fazemeright.chatbotmetcs622.network.models.NetResponse;

/*
 * Interface for handling API response
 * */
public interface NetworkCallback<T> {

    /**
     * Interface method called on success of api call
     *
     * @param response {@link NetResponse}
     */
    void onSuccess(NetResponse<T> response);

    /**
     * Interface method called on api/network failure
     *
     * @param error Error obj with error detail
     */
    void onError(NetError error);
}