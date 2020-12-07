package com.fazemeright.firebase_api_library.api.result;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.fazemeright.firebase_api_library.listeners.OnTaskCompleteListener;

public class ResultAdapterForBooleanLiveUpdates<T> implements OnTaskCompleteListener<T> {
  private final MutableLiveData<Result<Boolean>> mutableLiveData;

  public ResultAdapterForBooleanLiveUpdates(
      MutableLiveData<Result<Boolean>> mutableLiveData) {
    this.mutableLiveData = mutableLiveData;
  }

  @Override
  public void onComplete(@NonNull TaskResult<T> task) {
    if (task.isSuccessful()) {
      mutableLiveData.setValue(Result.withData(true));
    } else {
      mutableLiveData.setValue(Result.exception(task.getException()));
    }
  }
}
