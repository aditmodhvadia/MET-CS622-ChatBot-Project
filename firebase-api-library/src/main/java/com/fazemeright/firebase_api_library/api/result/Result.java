package com.fazemeright.firebase_api_library.api.result;

import javax.annotation.Nullable;

public class Result<T> implements TaskResult<T> {
  private final T data;
  private final Exception exception;

  public Result(T data) {
    this.data = data;
    exception = null;
  }

  public Result(Exception exception) {
    this.exception = exception;
    data = null;
  }

  @Override
  public boolean isSuccessful() {
    return data != null;
  }

  @Override
  public boolean isFailed() {
    return !this.isSuccessful();
  }

  @Nullable
  @Override
  public T getData() {
    return data;
  }

  @Nullable
  @Override
  public Exception getException() {
    return exception;
  }
}
