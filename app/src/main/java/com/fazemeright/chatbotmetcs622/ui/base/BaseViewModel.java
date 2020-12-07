package com.fazemeright.chatbotmetcs622.ui.base;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.fazemeright.chatbotmetcs622.network.ApiManager;
import com.fazemeright.chatbotmetcs622.network.NetworkManager;
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository;

public abstract class BaseViewModel extends AndroidViewModel {
  protected MessageRepository messageRepository;
  protected ApiManager apiManager;

  /**
   * Constructor.
   *
   * @param application application
   */
  public BaseViewModel(@NonNull Application application) {
    super(application);
    messageRepository = MessageRepository.getInstance(application);
    apiManager = ApiManager.getInstance();
    apiManager.init(NetworkManager.getInstance());
  }

  /**
   * Run on a UI safe thread.
   *
   * @param runnable runnable
   */
  public void runOnThread(Runnable runnable) {
    new Thread(runnable).start();
  }

  /**
   * Get username of the current logged in user.
   *
   * @return username if logged in, else <code>null</code>
   */
  public String getUserName() {
    return messageRepository.getUserName();
  }

  /**
   * Logs out the user.
   */
  public void logOutUser() {
    messageRepository.logOutUser();
  }
}
