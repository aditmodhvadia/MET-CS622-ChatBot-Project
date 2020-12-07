package com.fazemeright.chatbotmetcs622.ui.base;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.fazemeright.chatbotmetcs622.network.ApiManager;
import com.fazemeright.chatbotmetcs622.network.NetworkManager;
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository;

public abstract class BaseViewModel extends AndroidViewModel {
  protected MessageRepository mMessageRepository;
  protected ApiManager apiManager;

  public BaseViewModel(@NonNull Application application) {
    super(application);
    mMessageRepository = MessageRepository.getInstance(application);
    apiManager = ApiManager.getInstance();
    apiManager.init(NetworkManager.getInstance());
  }

  public void runOnThread(Runnable runnable) {
    new Thread(runnable).start();
  }

  /**
   * Get username of the current logged in user
   *
   * @return username if logged in, else <code>null</code>
   */
  public String getUserName() {
    return mMessageRepository.getUserName();
  }

  /**
   * Logs out the user
   */
  public void logOutUser() {
    mMessageRepository.logOutUser();
  }
}
