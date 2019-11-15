package com.fazemeright.chatbotmetcs622.ui.registration;

import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity;

public class RegistrationActivity extends BaseActivity {

    @Override
    public void initViews() {
//        set title for activity
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.registration));
        }
    }

    @Override
    public void setListeners() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_registration;
    }
}
