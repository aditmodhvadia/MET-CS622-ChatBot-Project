package com.fazemeright.chatbotmetcs622.ui.splash;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import com.fazemeright.chatbotmetcs622.ui.landing.LandingActivity;
import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity;
import com.fazemeright.chatbotmetcs622.ui.registration.RegistrationActivity;
import com.fazemeright.firebase_api_library.listeners.OnTaskCompleteListener;

import timber.log.Timber;

public class SplashActivity extends BaseActivity {

    @Override
    public void initViews() {
        hideSystemUI();
        TextView tvAppVersion = findViewById(R.id.tvAppVersion);

        tvAppVersion.setText(getAppVersion());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                determineIfUserIsLoggedIn();
            }
        }, 300);
    }

    private void determineIfUserIsLoggedIn() {
        apiManager.reloadUserAuthState(new OnTaskCompleteListener() {
            @Override
            public void onTaskSuccessful() {
//                user is logged in, open landing activity
                Timber.i("Open Landing Activity");
                openLandingActivity();
            }

            @Override
            public void onTaskCompleteButFailed(String errMsg) {
                //  user not logged in, open registration activity
                Timber.i("Open Registration Activity");
                openRegistrationActivity();
            }

            @Override
            public void onTaskFailed(Exception e) {
                //  user not logged in or could not perform check, open registration activity
                Timber.i("Open Registration Activity");
                openRegistrationActivity();
            }
        });
    }

    /**
     * Call to open RegistrationActivity from the current activity
     */
    private void openRegistrationActivity() {
        startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
        finish();
    }

    /**
     * Open LandingActivity and finish this one
     */
    private void openLandingActivity() {
        startActivity(new Intent(SplashActivity.this, LandingActivity.class));
        finish();
    }

    /**
     * Call to get the version of the Application
     *
     * @return version name of the application
     */
    private String getAppVersion() {
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "beta-testing";
        }
    }

    /**
     * Makes the screen layout to cover the full display of the device
     */
    private void hideSystemUI() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public void setListeners() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_splash;
    }
}
