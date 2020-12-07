package com.fazemeright.chatbotmetcs622.ui.registration;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.fazemeright.chatbotmetcs622.ui.base.BaseViewModel;
import com.fazemeright.firebase_api_library.api.result.Result;
import com.fazemeright.firebase_api_library.api.result.ResultAdapterForBooleanLiveUpdates;

public class RegistrationActivityViewModel extends BaseViewModel {
  private final MutableLiveData<Result<Boolean>> _userRegistered = new MutableLiveData<>();
  public LiveData<Result<Boolean>> userRegistered = _userRegistered;

  public RegistrationActivityViewModel(
      @NonNull Application application) {
    super(application);
  }

  public void registerNewUser(String email, String password, String firstName, String lastName) {
    runOnThread(() -> mMessageRepository
        .createNewUserAndStoreDetails(email, password, firstName, lastName,
            new ResultAdapterForBooleanLiveUpdates<>(_userRegistered)));
  }
}
