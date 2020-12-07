package com.fazemeright.chatbotmetcs622.ui.login;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel;
import javax.annotation.Nonnull;
import timber.log.Timber;

public class LoginActivityViewModel extends BaseViewModel {
  private MutableLiveData<Boolean> _userSignedIn = new MutableLiveData<>();
  public LiveData<Boolean> userSignedIn = _userSignedIn;

  public LoginActivityViewModel(@NonNull Application application) {
    super(application);
  }

  public void signInWithEmailPassword(@Nonnull String email, @Nonnull String password) {
    runOnThread(() -> mMessageRepository.getUserAuthentication()
        .signInWithEmailAndPassword(email, password, userAuthResult -> {
          if (userAuthResult.isSuccessful()) {
            _userSignedIn.setValue(true);
          } else {
            Timber.e(userAuthResult.getException());
            _userSignedIn.setValue(false);
          }
        }));
  }
}
