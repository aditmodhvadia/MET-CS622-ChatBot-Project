package com.fazemeright.chatbotmetcs622.network.models;

/**
 * To use when we want to have sync response from network manager
 *
 * @param <T>
 */
public class NetCompoundRes<T> {

  private boolean isSuccess;
  private NetResponse<T> netResponse;
  private NetError netError;

  public void setSuccess(boolean success) {
    isSuccess = success;
  }

  public void setNetResponse(NetResponse<T> netResponse) {
    this.netResponse = netResponse;
  }

  public void setNetError(NetError netError) {
    this.netError = netError;
  }

  public boolean isSuccess() {
    return isSuccess;
  }

  public NetResponse<T> getNetResponse() {
    return netResponse;
  }

  public NetError getNetError() {
    return netError;
  }
}
