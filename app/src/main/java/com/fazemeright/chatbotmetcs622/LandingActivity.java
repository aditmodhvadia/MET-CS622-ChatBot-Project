package com.fazemeright.chatbotmetcs622;

import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity;

public class LandingActivity extends BaseActivity {

    @Override
    public void initViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.welcome_title) + " " + apiManager.getCurrentLoggedInUserEmail());
        }
    }

    @Override
    public void setListeners() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_landing;
    }
}
