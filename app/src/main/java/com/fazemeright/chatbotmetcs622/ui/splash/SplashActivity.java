package com.fazemeright.chatbotmetcs622.ui.splash;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity;
import com.fazemeright.firebase_api_library.listeners.OnTaskCompleteListener;

import timber.log.Timber;

public class SplashActivity extends BaseActivity {

    @Override
    public void initViews() {
        TextView tvAppVersion = findViewById(R.id.tvAppVersion);

        tvAppVersion.setText(getAppVersion());

        determineIfUserIsLoggedIn();
    }

    private void determineIfUserIsLoggedIn() {
        apiManager.reloadUserAuthState(new OnTaskCompleteListener() {
            @Override
            public void onTaskSuccessful() {
                //  open login activity
                Timber.i("Open Landing Activity");
            }

            @Override
            public void onTaskCompleteButFailed(String errMsg) {
                //  open registration activity
                Timber.i("Open Login Activity");
            }

            @Override
            public void onTaskFailed(Exception e) {
                //  open registration activity
                Timber.i("Open Login Activity");
            }
        });
    }

    private String getAppVersion() {
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "beta-testing";
        }
    }

    @Override
    public void setListeners() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_splash;
    }
}
