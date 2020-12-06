package com.fazemeright.firebase_api_library.listeners;

import androidx.annotation.NonNull;
import com.fazemeright.firebase_api_library.api.result.TaskResult;

public interface OnCompleteListenerNew<T> {
  void onComplete(@NonNull TaskResult<T> task);
}


