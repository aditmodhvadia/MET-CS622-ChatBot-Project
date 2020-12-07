package com.fazemeright.chatbotmetcs622.ui.splash;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel;

public class SplashActivityViewModel extends BaseViewModel {
  private final MutableLiveData<Boolean> _userAuthState = new MutableLiveData<>();
  public LiveData<Boolean> userAuthState = _userAuthState;

  public SplashActivityViewModel(@NonNull Application application) {
    super(application);
    mMessageRepository.getUserAuthentication().reloadCurrentUserAuthState(
        result -> _userAuthState.setValue(result.isSuccessful()));
  }


}
