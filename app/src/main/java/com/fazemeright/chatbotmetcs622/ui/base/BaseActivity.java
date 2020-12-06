package com.fazemeright.chatbotmetcs622.ui.base;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.network.ApiManager;
import com.fazemeright.chatbotmetcs622.network.NetworkManager;
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository;

public abstract class BaseActivity extends AppCompatActivity {

  public Context mContext;
  protected MessageRepository messageRepository;
  protected ApiManager apiManager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mContext = this;
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
   * @param activity calling activity
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

  public void showKeyBoard(View view) {
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm != null) {
      imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
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

  /** Template method to initialize views of activity */
  public void initViews(){}

  /** Template method to set listeners of view or callback */
  public void setListeners(){}

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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    @MenuRes int menuId = getMenuId();
    if(menuId != 0) {
      getMenuInflater().inflate(menuId, menu);
    }
    return super.onCreateOptionsMenu(menu);
  }

  /**
   * Template method to get Menu Resource Id
   *
   * @return <code>0</code> if no menu item needed to inflate, else <code>override</code> by sub activity
   */
  public int getMenuId() {
    return 0;
  }
}
