package com.fazemeright.chatbotmetcs622.network.models;


import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * Class for handling Network/API related errors
 * */
public class NetError extends Exception {

    private String errorBody = "";
    private int errorCode = ChatBotError.ChatBotErrorCodes.SOMETHING_WENT_WRONG;
    private String errorDetail = "";
    private String errorLocalizeMessage = "";
    private Object apiRequest;
    private String requestName = "";
    private String responseErrorMessage;

    public String getResponseErrorMessage() {
        return responseErrorMessage;
    }

    public void setResponseErrorMessage(String responseErrorMessage) {
        this.responseErrorMessage = parseJson(responseErrorMessage);
    }

    private String parseJson(String responseErrorMessage) {
        String errorMessage = "Something went wrong!";
        if (!TextUtils.isEmpty(responseErrorMessage)) {
            try {
                JSONObject jsonObject = new JSONObject(responseErrorMessage);
                if (jsonObject.has("responseMessage")) {
                    errorMessage = jsonObject.getString("responseMessage");
                } else {
                    return responseErrorMessage;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return responseErrorMessage;
            }
        }
        return errorMessage;

    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        if (requestName != null) {
            this.requestName = requestName;
        }
    }

    public Object getApiRequest() {
        return apiRequest;
    }

    public void setApiRequest(Object apiRequest) {
        this.apiRequest = apiRequest;
    }

    public String getErrorLocalizeMessage() {
        return errorLocalizeMessage;
    }

    public void setErrorLocalizeMessage(String errorLocalizeMessage) {
        if (errorLocalizeMessage != null) {
            this.errorLocalizeMessage = errorLocalizeMessage;
        }
    }

    public NetError(String message) {
        super(message);
        if (message == null) {
            message = "Getting null error message.";
        }
        setErrorLocalizeMessage(message);
        setErrorBody(message);
        setErrorDetail(message);
    }

    public void setErrorDetail(String errorDetail) {
        if (errorDetail != null) {
            this.errorDetail = errorDetail;
        }
    }

    public String getErrorDetail() {
        return this.errorDetail;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorBody() {
        return errorBody;
    }

    public void setErrorBody(String errorBody) {
        if (errorBody != null) {
            this.errorBody = errorBody;
        }
    }
}
