package com.fazemeright.chatbotmetcs622.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Class for handling generic response.
 */
public class NetResponse<T> {

  @SerializedName("response")
  private T response;

  public T getResponse() {
    return response;
  }

  public void setResponse(T response) {
    this.response = response;
  }
}
