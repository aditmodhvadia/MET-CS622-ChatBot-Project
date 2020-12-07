package com.fazemeright.chatbotmetcs622.ui.registration;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity;
import com.fazemeright.chatbotmetcs622.ui.landing.LandingActivity;
import com.fazemeright.chatbotmetcs622.ui.login.LoginActivity;
import com.fazemeright.chatbotmetcs622.utils.AppUtils;

public class RegistrationActivity extends BaseActivity<RegistrationActivityViewModel>
    implements View.OnClickListener {

  private EditText userEmailEditText,
      userPasswordEditText,
      userConPasswordEditText,
      etFirstName,
      etLastName;
  private TextView tvHaveAccount;
  private Button btnRegister;

  @Override
  protected Class<RegistrationActivityViewModel> getViewModelClass() {
    return RegistrationActivityViewModel.class;
  }

  @Override
  public void initViews() {
    setUpSupportActionBar();

    userEmailEditText = findViewById(R.id.userLoginEmailEditText);
    etFirstName = findViewById(R.id.etFirstName);
    etLastName = findViewById(R.id.etLastName);
    userPasswordEditText = findViewById(R.id.userPasswordEditText);
    userConPasswordEditText = findViewById(R.id.userConPasswordEditText);
    tvHaveAccount = findViewById(R.id.tvHaveAccount);
    btnRegister = findViewById(R.id.btnRegister);

    viewModel.userRegistered.observe(this, userRegistered -> {
      if (userRegistered) {
        btnRegister.setText(getString(R.string.registration_success_msg));
        openLandingActivity();
      }
    });
  }

  private void setUpSupportActionBar() {
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(getString(R.string.registration));
    }
  }

  @Override
  public void setListeners() {
    btnRegister.setOnClickListener(this);
    tvHaveAccount.setOnClickListener(this);
  }

  @Override
  public int getLayoutResId() {
    return R.layout.activity_registration;
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    if (id == R.id.tvHaveAccount) {
      openLoginActivity();
    } else if (id == R.id.btnRegister) {
      disableButton(btnRegister);
      registerUser();
      enableButton(btnRegister);
    }
  }

  private void registerUser() {
    String email = userEmailEditText.getText().toString();
    String firstName = etFirstName.getText().toString();
    String lastName = etLastName.getText().toString();
    String password = userPasswordEditText.getText().toString();
    String conPassword = userConPasswordEditText.getText().toString();
    performRegistration(email, firstName, lastName, password, conPassword);
  }

  /**
   * Open Login Activity
   */
  private void openLoginActivity() {
    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
  }

  /**
   * Call to perform validation on the input parameters and then perform registration
   *
   * @param email       user email address
   * @param firstName   first name of user
   * @param lastName    last name of user
   * @param password    user selected password
   * @param conPassword user selected confirmation password
   */
  private void performRegistration(
      final String email,
      String firstName,
      String lastName,
      final String password,
      String conPassword) {
    if (!AppUtils.isValidEmail(email)) {
      userEmailEditText.setError(mContext.getString(R.string.incorrect_email_err_msg));
      userEmailEditText.requestFocus();
      return;
    }

    if (!AppUtils.isValidName(firstName)) {
      etFirstName.setError(mContext.getString(R.string.incorrect_first_name));
      etFirstName.requestFocus();
      return;
    }

    if (!AppUtils.isValidName(lastName)) {
      etLastName.setError(mContext.getString(R.string.incorrect_last_name));
      etLastName.requestFocus();
      return;
    }

    if (!AppUtils.isValidPassword(password)) {
      userPasswordEditText.setError(mContext.getString(R.string.incorrect_pass_err_msg));
      userPasswordEditText.requestFocus();
      return;
    }

    if (!AppUtils.arePasswordsValid(password, conPassword)) {
      userPasswordEditText.setError(mContext.getString(R.string.passwords_dont_match_err_msg));
      userPasswordEditText.requestFocus();
      userPasswordEditText.setText("");
      userConPasswordEditText.setText("");
      return;
    }

    viewModel.registerNewUser(
        email,
        password,
        firstName,
        lastName);
  }

  /**
   * Open LandingActivity and finish this one
   */
  private void openLandingActivity() {
    startActivity(new Intent(RegistrationActivity.this, LandingActivity.class));
    finish();
  }
}
