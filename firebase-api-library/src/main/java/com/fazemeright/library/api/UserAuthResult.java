package com.fazemeright.library.api;

import androidx.annotation.Nullable;

public interface UserAuthResult {

  //  TODO: Replace with custom user object
  @Nullable
  String getUser();

  @Nullable
  String getErrorMsg();
}
