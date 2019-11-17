package com.fazemeright.chatbotmetcs622.ui.login;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fazemeright.chatbotmetcs622.LandingActivity;
import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity;
import com.fazemeright.chatbotmetcs622.ui.registration.RegistrationActivity;
import com.fazemeright.chatbotmetcs622.utils.AppUtils;
import com.fazemeright.firebase_api_library.listeners.OnTaskCompleteListener;

import timber.log.Timber;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText userEmailEditText, userPasswordEditText;
    private TextView tvDontHaveAccount;
    private Button btnLogin;

    @Override
    public void initViews() {
//        set title for activity
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.login_title));
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        userEmailEditText = findViewById(R.id.userLoginEmailEditText);
        userPasswordEditText = findViewById(R.id.userPasswordEditText);
        tvDontHaveAccount = findViewById(R.id.tvDontHaveAccount);
        btnLogin = findViewById(R.id.btnLogin);
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
        tvDontHaveAccount.setOnClickListener(this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDontHaveAccount:
                openRegistrationActivity();
                break;
            case R.id.btnLogin:
//                TODO: Disable and re-enable the button after performing registration and validation
                disableButton(btnLogin);
                String email = userEmailEditText.getText().toString();
                String password = userPasswordEditText.getText().toString();
                performLogin(email, password);
                enableButton(btnLogin);
                break;
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

        apiManager.logInWithEmailAndPassword(email, password, new OnTaskCompleteListener() {
            @Override
            public void onTaskSuccessful() {
                Timber.i("User logged in successfully %s", apiManager.getCurrentLoggedInUserEmail());
                btnLogin.setText(getString(R.string.login_success_msg));
                openLandingActivity();
            }

            @Override
            public void onTaskCompleteButFailed(String errMsg) {
                Timber.e(errMsg);
//                TODO: Show error to user
            }

            @Override
            public void onTaskFailed(Exception e) {
                Timber.e(e);
//                TODO: Show error to user
            }
        });
    }

    /**
     * Open LandingActivity and finish this one
     */
    private void openLandingActivity() {
        startActivity(new Intent(LoginActivity.this, LandingActivity.class));
        finish();
    }

    /**
     * Open Registration Activity
     */
    private void openRegistrationActivity() {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
        finish();
    }
}
