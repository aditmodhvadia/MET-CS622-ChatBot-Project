package com.fazemeright.chatbotmetcs622.ui.splash;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel;
import com.fazemeright.firebase_api_library.api.result.Result;
import com.fazemeright.firebase_api_library.api.result.ResultAdapterForBooleanLiveUpdates;

public class SplashActivityViewModel extends BaseViewModel {
  private final MutableLiveData<Result<Boolean>> _userAuthState = new MutableLiveData<>();
  public LiveData<Result<Boolean>> userAuthState = _userAuthState;

  public SplashActivityViewModel(@NonNull Application application) {
    super(application);
    mMessageRepository.getUserAuthentication()
        .reloadCurrentUserAuthState(new ResultAdapterForBooleanLiveUpdates<>(_userAuthState));
  }
}
