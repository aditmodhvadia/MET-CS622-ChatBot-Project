package com.fazemeright.chatbotmetcs622.ui.login;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.intentservice.FireBaseIntentService;
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity;
import com.fazemeright.chatbotmetcs622.ui.landing.LandingActivity;
import com.fazemeright.chatbotmetcs622.utils.AppUtils;
import timber.log.Timber;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

  private EditText userEmailEditText, userPasswordEditText;
  private TextView tvDoNotHaveAccount;
  private Button btnLogin;

  @Override
  public void initViews() {
    setUpSupportActionBar();

    userEmailEditText = findViewById(R.id.userLoginEmailEditText);
    userPasswordEditText = findViewById(R.id.userPasswordEditText);
    tvDoNotHaveAccount = findViewById(R.id.tvDontHaveAccount);
    btnLogin = findViewById(R.id.btnLogin);
  }

  private void setUpSupportActionBar() {
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(getString(R.string.login_title));
      getSupportActionBar().setHomeButtonEnabled(true);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void setListeners() {
    btnLogin.setOnClickListener(this);
    tvDoNotHaveAccount.setOnClickListener(this);
  }

  @Override
  public int getLayoutResId() {
    return R.layout.activity_login;
  }

  @Override
  public void onClick(View v) {
    int clickedViewId = v.getId();
    if (clickedViewId == R.id.tvDontHaveAccount) {
      goToRegistrationActivity();
    } else if (clickedViewId == R.id.btnLogin) {
      disableButton(btnLogin);
      performLogin(userEmailEditText.getText().toString().trim(),
          userPasswordEditText.getText().toString().trim());
      enableButton(btnLogin);
    }
  }

  /**
   * Perform login with the given credentials
   *
   * @param email    user email address
   * @param password user password
   */
  private void performLogin(String email, String password) {
    if (!AppUtils.isValidEmail(email)) {
      userEmailEditText.setError(mContext.getString(R.string.incorrect_email_err_msg));
      userEmailEditText.requestFocus();
      return;
    }

    if (!AppUtils.isValidPassword(password)) {
      userPasswordEditText.setError(mContext.getString(R.string.incorrect_pass_err_msg));
      userPasswordEditText.requestFocus();
      return;
    }

    Timber.i("Login clicked");
    messageRepository.getUserAuthentication().signInWithEmailAndPassword(
        email,
        password, userAuthResult -> {
          if (userAuthResult.isSuccessful()) {
            Timber.i(
                "User logged in successfully %s",
                messageRepository.getUserAuthentication().getCurrentUserEmail());
            btnLogin.setText(getString(R.string.login_success_msg));
            Intent intent = new Intent(mContext, FireBaseIntentService.class);
            intent.putExtra(
                FireBaseIntentService.Actions.ACTION,
                FireBaseIntentService.Actions.ACTION_SYNC_MESSAGES);
            ContextCompat.startForegroundService(mContext, intent);
            openLandingActivity();
          } else {
            Timber.e(userAuthResult.getException());
          }
        });
  }

  /**
   * Open LandingActivity and finish this one
   */
  private void openLandingActivity() {
    startActivity(new Intent(LoginActivity.this, LandingActivity.class));
    finishAffinity();
  }

  /**
   * Go to Registration Activity
   */
  private void goToRegistrationActivity() {
    finish();
  }
}
