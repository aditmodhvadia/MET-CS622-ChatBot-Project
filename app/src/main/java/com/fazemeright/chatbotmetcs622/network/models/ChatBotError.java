package com.fazemeright.chatbotmetcs622.network.models;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ChatBotError {

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({
      ChatBotErrorCodes.INTERNET_NOT_AVAILABLE, /*SOMETHING_WENT_WRONG,*/
      ChatBotErrorCodes.INVALID_URL,
      ChatBotErrorCodes.DATA_NOT_FOUND,
      ChatBotErrorCodes.BAD_REQUEST,
      ChatBotErrorCodes.UN_AUTHORIZED,
      ChatBotErrorCodes.UN_EXPECTED_SERVER_ERROR
  })
  public @interface ChatBotErrorCodes {

    /**
     * Internet is not available
     */
    int INTERNET_NOT_AVAILABLE = 1001;

    /**
     * Invalid URL
     */
    int INVALID_URL = 1002;

    /**
     * Used for unknown error
     */
    int SOMETHING_WENT_WRONG = -123456;

    /**
     * Used for data not found error
     */
    int DATA_NOT_FOUND = 204;

    /**
     * Used for Bad request (Malformed Parameters or parameters missing)
     */
    int BAD_REQUEST = 400;

    /**
     * Used for Unauthorized (Authorization header is incorrect, log-in user again)
     */
    int UN_AUTHORIZED = 401;

    /**
     * Used for Unexpected server error (Error)
     */
    int UN_EXPECTED_SERVER_ERROR = 500;
  }
}
