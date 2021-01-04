package com.fazemeright.chatbotmetcs622.ui.login;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel;
import com.fazemeright.library.api.result.Result;
import com.fazemeright.library.api.result.ResultAdapterForBooleanLiveUpdates;
import javax.annotation.Nonnull;

public class LoginActivityViewModel extends BaseViewModel {
  private final MutableLiveData<Result<Boolean>> userSignedInMutable = new MutableLiveData<>();
  public LiveData<Result<Boolean>> userSignedIn = userSignedInMutable;

  /**
   * Constructor.
   *
   * @param application application
   */
  public LoginActivityViewModel(@NonNull Application application) {
    super(application);
  }

  /**
   * Sign in user with email and password.
   *
   * @param email    email
   * @param password password
   */
  public void signInWithEmailPassword(@Nonnull String email, @Nonnull String password) {
    runOnThread(() -> messageRepository.signInWithEmailAndPassword(email, password,
        new ResultAdapterForBooleanLiveUpdates<>(userSignedInMutable)));
  }
}
