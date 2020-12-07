package com.fazemeright.chatbotmetcs622.ui.login;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel;
import com.fazemeright.firebase_api_library.api.result.Result;
import com.fazemeright.firebase_api_library.api.result.ResultAdapterForBooleanLiveUpdates;
import javax.annotation.Nonnull;

public class LoginActivityViewModel extends BaseViewModel {
  private final MutableLiveData<Result<Boolean>> _userSignedIn = new MutableLiveData<>();
  public LiveData<Result<Boolean>> userSignedIn = _userSignedIn;

  public LoginActivityViewModel(@NonNull Application application) {
    super(application);
  }

  public void signInWithEmailPassword(@Nonnull String email, @Nonnull String password) {
    runOnThread(() -> mMessageRepository.signInWithEmailAndPassword(email, password,
        new ResultAdapterForBooleanLiveUpdates<>(_userSignedIn)));
  }
}
