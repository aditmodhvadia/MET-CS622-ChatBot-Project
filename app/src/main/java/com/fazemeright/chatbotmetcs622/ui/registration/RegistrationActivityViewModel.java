package com.fazemeright.chatbotmetcs622.ui.registration;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel;

public class RegistrationActivityViewModel extends BaseViewModel {
  private MutableLiveData<Boolean> _userRegistered = new MutableLiveData<>();
  public LiveData<Boolean> userRegistered = _userRegistered;

  public RegistrationActivityViewModel(
      @NonNull Application application) {
    super(application);
  }

  public void registerNewUser(String email, String password, String firstName, String lastName) {
    runOnThread(() -> mMessageRepository
        .createNewUserAndStoreDetails(email, password, firstName, lastName,
            result -> _userRegistered.setValue(result.isSuccessful())));
  }
}
