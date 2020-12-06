package com.fazemeright.firebase_api_library.api.firebase;

import androidx.annotation.NonNull;
import com.fazemeright.firebase_api_library.api.result.Result;
import com.fazemeright.firebase_api_library.listeners.OnCompleteListenerNew;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class OnTaskCompleteAdapterForOnComplete<T> implements OnCompleteListener<T> {
  private final OnCompleteListenerNew<T> onTaskCompleteListener;

  public OnTaskCompleteAdapterForOnComplete(
      @NonNull OnCompleteListenerNew<T> onTaskCompleteListener) {
    this.onTaskCompleteListener = onTaskCompleteListener;
  }

  @Override
  public void onComplete(@NonNull Task<T> task) {
    if (task.isSuccessful()) {
      this.onTaskCompleteListener.onComplete(Result.withData(task.getResult()));
    } else {
      this.onTaskCompleteListener.onComplete(Result.exception(task.getException()));
    }
  }
}
