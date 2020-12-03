package com.fazemeright.firebase_api_library.api;

import androidx.annotation.Nullable;

public interface UserAuthResult {

  //  TODO: Replace with custom user object
  @Nullable
  String getUser();

  @Nullable
  String getErrorMsg();
}
