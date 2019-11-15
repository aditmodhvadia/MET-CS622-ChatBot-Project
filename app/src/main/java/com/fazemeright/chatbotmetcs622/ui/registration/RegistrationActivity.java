package com.fazemeright.chatbotmetcs622.ui.registration;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity;
import com.fazemeright.chatbotmetcs622.utils.AppUtils;
import com.fazemeright.firebase_api_library.listeners.OnTaskCompleteListener;

import timber.log.Timber;

public class RegistrationActivity extends BaseActivity implements View.OnClickListener {

    private EditText userEmailEditText, userPasswordEditText, userConPasswordEditText;
    private TextView tvHaveAccount;
    private Button btnRegister;

    @Override
    public void initViews() {
//        set title for activity
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.registration));
        }

        userEmailEditText = findViewById(R.id.userLoginEmailEditText);
        userPasswordEditText = findViewById(R.id.userPasswordEditText);
        userConPasswordEditText = findViewById(R.id.userConPasswordEditText);
        tvHaveAccount = findViewById(R.id.tvHaveAccount);
        btnRegister = findViewById(R.id.btnRegister);

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
        switch (v.getId()) {
            case R.id.tvHaveAccount:
//                TODO: Open LoginActivity
                break;
            case R.id.btnRegister:
//                TODO: Disable and re-enable the button after performing registration and validation
                disableRegistrationButton();
                String email = userEmailEditText.getText().toString();
                String password = userEmailEditText.getText().toString();
                String conPassword = userEmailEditText.getText().toString();
                performRegistration(email, password, conPassword);
                enableRegistrationButton();
                break;
        }

    }

    /**
     * Call to perform validation on the input parameters and then perform registration
     *
     * @param email       user email address
     * @param password    user selected password
     * @param conPassword user selected confirmation password
     */
    private void performRegistration(final String email, final String password, String conPassword) {
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

        if (!AppUtils.arePasswordsValid(password, conPassword)) {
            userPasswordEditText.setError(mContext.getString(R.string.passwords_dont_match_err_msg));
            userPasswordEditText.requestFocus();
            userPasswordEditText.setText("");
            userConPasswordEditText.setText("");
            return;
        }

        apiManager.registerNewUserWithEmailPassword(email, password, new OnTaskCompleteListener() {
            @Override
            public void onTaskSuccessful() {
                Timber.i("New user registered successfully %s", apiManager.getCurrentLoggedInUserEmail());
                btnRegister.setText(getString(R.string.registration_success_msg));
            }

            @Override
            public void onTaskCompleteButFailed(String errMsg) {
                Timber.i(errMsg);
            }

            @Override
            public void onTaskFailed(Exception e) {
                Timber.i(e);
            }
        });

    }

    /**
     * Enable Registration button
     */
    private void enableRegistrationButton() {
        btnRegister.setEnabled(true);
    }

    /**
     * Disable Registration button
     */
    private void disableRegistrationButton() {
        btnRegister.setEnabled(false);
    }
}
