package com.fazemeright.firebase_api_library.listeners;

public interface OnTaskCompleteListener<T> {

  void onTaskSuccessful(T result);

  void onTaskCompleteButFailed(T result);

  void onTaskFailed(Exception e);
}
