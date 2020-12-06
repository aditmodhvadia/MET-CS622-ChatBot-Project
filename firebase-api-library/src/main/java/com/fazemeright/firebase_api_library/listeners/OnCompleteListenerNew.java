package com.fazemeright.firebase_api_library.listeners;

import androidx.annotation.NonNull;

public interface OnCompleteListenerNew<T> {
  void onComplete(@NonNull Result<T> task);
}


