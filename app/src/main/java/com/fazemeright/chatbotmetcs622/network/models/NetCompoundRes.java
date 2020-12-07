package com.fazemeright.chatbotmetcs622.network.models;

/**
 * To use when we want to have sync response from network manager.
 *
 * @param <T> type
 */
public class NetCompoundRes<T> {

  private boolean isSuccess;
  private NetResponse<T> netResponse;
  private NetError netError;

  public boolean isSuccess() {
    return isSuccess;
  }

  public void setSuccess(boolean success) {
    isSuccess = success;
  }

  public NetResponse<T> getNetResponse() {
    return netResponse;
  }

  public void setNetResponse(NetResponse<T> netResponse) {
    this.netResponse = netResponse;
  }

  public NetError getNetError() {
    return netError;
  }

  public void setNetError(NetError netError) {
    this.netError = netError;
  }
}
