package com.fazemeright.chatbotmetcs622.ui.base;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.fazemeright.chatbotmetcs622.network.ApiManager;
import com.fazemeright.chatbotmetcs622.network.NetworkManager;
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository;

public abstract class BaseViewModel extends AndroidViewModel {
  public MessageRepository mMessageRepository;
  protected ApiManager apiManager;

  public BaseViewModel(@NonNull Application application) {
    super(application);
    mMessageRepository = MessageRepository.getInstance(application);
    apiManager = ApiManager.getInstance();
    apiManager.init(NetworkManager.getInstance());
  }
}
