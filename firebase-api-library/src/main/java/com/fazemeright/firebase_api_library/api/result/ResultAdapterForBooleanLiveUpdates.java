package com.fazemeright.firebase_api_library.api.result;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.fazemeright.firebase_api_library.listeners.OnTaskCompleteListener;

public class ResultAdapterForBooleanLiveUpdates<T> implements OnTaskCompleteListener<T> {
  private final MutableLiveData<Result<Boolean>> _mutableLiveData;

  public ResultAdapterForBooleanLiveUpdates(
      MutableLiveData<Result<Boolean>> mutableLiveData) {
    _mutableLiveData = mutableLiveData;
  }

  @Override
  public void onComplete(@NonNull TaskResult<T> task) {
    if (task.isSuccessful()) {
      _mutableLiveData.setValue(Result.withData(true));
    } else {
      _mutableLiveData.setValue(Result.exception(task.getException()));
    }
  }
}
