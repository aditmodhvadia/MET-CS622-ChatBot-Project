package com.fazemeright.chatbotmetcs622.ui.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.fazemeright.chatbotmetcs622.network.ApiManager;
import com.fazemeright.chatbotmetcs622.network.NetworkManager;
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository;

public abstract class BaseFragment extends Fragment {

  public Context mContext;
  protected MessageRepository messageRepository;
  protected ApiManager apiManager;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mContext = getActivity();
    messageRepository = MessageRepository.getInstance(mContext);
    apiManager = ApiManager.getInstance();
    apiManager.init(NetworkManager.getInstance());
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(getLayoutResId(), container, false);
    initViews(view);
    setListeners(view);
    return view;
  }

  /**
   * To get layout resource id
   */
  public abstract @LayoutRes
  int getLayoutResId();

  /**
   * To initialize views of activity
   *
   * @param view inflated layout
   */
  public abstract void initViews(View view);

  /**
   * To set listeners of view or callback
   *
   * @param view inflated layout
   */
  public abstract void setListeners(View view);

  public boolean isNetworkConnected() {
    ConnectivityManager cm =
        (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = null;
    if (cm != null) {
      networkInfo = cm.getActiveNetworkInfo();
    }
    return networkInfo != null && networkInfo.isConnected();
  }

  @Nullable
  @Override
  public Context getContext() {
    return mContext;
  }
}
