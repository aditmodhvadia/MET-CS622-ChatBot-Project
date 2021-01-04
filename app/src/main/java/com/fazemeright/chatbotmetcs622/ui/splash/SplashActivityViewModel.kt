package com.fazemeright.chatbotmetcs622.ui.splash;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel;
import com.fazemeright.library.api.result.Result;
import com.fazemeright.library.api.result.ResultAdapterForBooleanLiveUpdates;

public class SplashActivityViewModel extends BaseViewModel {
  private final MutableLiveData<Result<Boolean>> userAuthStateMutable = new MutableLiveData<>();
  public LiveData<Result<Boolean>> userAuthState = userAuthStateMutable;

  public SplashActivityViewModel(@NonNull Application application) {
    super(application);
    observeForUserAuthenticationState();
  }

  /**
   * Observe for user authentication state changes and report it.
   */
  private void observeForUserAuthenticationState() {
    messageRepository
        .reloadCurrentUserAuthState(new ResultAdapterForBooleanLiveUpdates<>(userAuthStateMutable));
  }
}
