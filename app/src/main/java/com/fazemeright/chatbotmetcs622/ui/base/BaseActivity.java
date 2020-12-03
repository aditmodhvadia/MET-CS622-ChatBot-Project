package com.fazemeright.chatbotmetcs622.ui.base;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fazemeright.chatbotmetcs622.network.ApiManager;
import com.fazemeright.chatbotmetcs622.network.NetworkManager;
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository;
import com.fazemeright.firebase_api_library.api.firebase.FireBaseApiManager;

public abstract class BaseActivity extends AppCompatActivity {

  public Context mContext;
  protected FireBaseApiManager fireBaseApiManager;
  protected MessageRepository messageRepository;
  protected ApiManager apiManager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mContext = this;
    fireBaseApiManager = FireBaseApiManager.getInstance();
    messageRepository = MessageRepository.getInstance(mContext);
    apiManager = ApiManager.getInstance();
    apiManager.init(NetworkManager.getInstance());
    setContentView(getLayoutResId());
  }

  @Override
  public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);
    initViews();
    setListeners();
  }

  /**
   * Call to hide soft keyboard
   *
   * @param activity
   */
  public void hideKeyboard(Activity activity) {
    InputMethodManager imm =
        (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
    // Find the currently focused view, so we can grab the correct window token from it.
    View view = activity.getCurrentFocus();
    // If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
      view = new View(activity);
    }
    if (imm != null) {
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  public void showKeyBoard(EditText yourEditText) {
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm != null) {
      imm.showSoftInput(yourEditText, InputMethodManager.SHOW_IMPLICIT);
    }
  }

  /**
   * Call to disable the given button
   *
   * @param button given button
   */
  protected void disableButton(Button button) {
    button.setEnabled(false);
  }

  /**
   * Call to enable the given button
   *
   * @param button given button
   */
  protected void enableButton(Button button) {
    button.setEnabled(true);
  }

  /** To initialize views of activity */
  public abstract void initViews();

  /** To set listeners of view or callback */
  public abstract void setListeners();

  /** To get layout resource id */
  public abstract int getLayoutResId();

  public boolean isNetworkConnected() {
    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = null;
    if (cm != null) {
      networkInfo = cm.getActiveNetworkInfo();
    }
    return networkInfo != null && networkInfo.isConnected();
  }

  public Context getContext() {
    return mContext;
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }
}
