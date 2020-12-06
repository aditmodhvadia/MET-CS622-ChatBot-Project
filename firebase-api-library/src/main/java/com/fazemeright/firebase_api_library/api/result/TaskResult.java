package com.fazemeright.firebase_api_library.api.result;

import javax.annotation.Nullable;

public interface TaskResult<T> {
  boolean isSuccessful();

  boolean isFailed();

  @Nullable
  T getData();

  @Nullable
  Exception getException();
}
