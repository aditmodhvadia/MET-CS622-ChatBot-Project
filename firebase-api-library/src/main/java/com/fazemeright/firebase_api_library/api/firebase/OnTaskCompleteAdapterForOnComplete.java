package com.fazemeright.firebase_api_library.api.firebase;

import androidx.annotation.NonNull;
import com.fazemeright.firebase_api_library.listeners.OnTaskCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class OnTaskCompleteAdapterForOnComplete<T> implements OnCompleteListener<T> {
  private final OnTaskCompleteListener<T> onTaskCompleteListener;

  public OnTaskCompleteAdapterForOnComplete(
      @NonNull OnTaskCompleteListener<T> onTaskCompleteListener) {
    this.onTaskCompleteListener = onTaskCompleteListener;
  }

  @Override
  public void onComplete(@NonNull Task<T> task) {
    if (task.isSuccessful()) {
      this.onTaskCompleteListener.onTaskSuccessful(task.getResult());
    } else if (task.isCanceled()) {
      this.onTaskCompleteListener.onTaskFailed(task.getException());
    } else {
      this.onTaskCompleteListener.onTaskCompleteButFailed(task.getResult());
    }
  }
}
