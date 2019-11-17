package com.fazemeright.chatbotmetcs622;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity;
import com.fazemeright.chatbotmetcs622.ui.login.LoginActivity;

public class LandingActivity extends BaseActivity {

    @Override
    public void initViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.welcome_title) + " " + apiManager.getCurrentLoggedInUserEmail());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logoutUser();
                openLoginActivity();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openLoginActivity() {
        startActivity(new Intent(LandingActivity.this, LoginActivity.class));
        finish();
    }

    private void logoutUser() {
        apiManager.logOutUser();
    }

    @Override
    public void setListeners() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_landing;
    }
}
