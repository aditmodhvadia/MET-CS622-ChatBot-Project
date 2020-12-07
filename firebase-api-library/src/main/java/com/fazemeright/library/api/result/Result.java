package com.fazemeright.library.api.result;

import javax.annotation.Nullable;

public class Result<T> implements TaskResult<T> {
  private final T data;
  private final Exception exception;

  private Result(T data) {
    this.data = data;
    exception = null;
  }

  private Result(Exception exception) {
    this.exception = exception;
    data = null;
  }

  public static Result<Void> nullResult() {
    return new Result<>(null);
  }

  public static <R> Result<R> exception(Exception exception) {
    return new Result<R>(exception);
  }

  public static <R> Result<R> withData(R data) {
    return new Result<>(data);
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
