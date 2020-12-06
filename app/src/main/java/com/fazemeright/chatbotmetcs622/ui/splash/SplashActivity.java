package com.fazemeright.chatbotmetcs622.ui.splash;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.annotation.AnimRes;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.fazemeright.chatbotmetcs622.R;
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity;
import com.fazemeright.chatbotmetcs622.ui.landing.LandingActivity;
import com.fazemeright.chatbotmetcs622.ui.registration.RegistrationActivity;
import com.fazemeright.chatbotmetcs622.workers.FireBaseSyncWorker;
import com.fazemeright.firebase_api_library.listeners.OnTaskCompleteListener;
import java.util.concurrent.TimeUnit;
import timber.log.Timber;

public class SplashActivity extends BaseActivity {

  private TextView tvAppVersion, tvAppTitle;

  @Override
  public void initViews() {
    hideSystemUI();
    tvAppVersion = findViewById(R.id.tvAppVersion);
    tvAppTitle = findViewById(R.id.tvAppTitle);

    tvAppVersion.setText(getAppVersion());

    startAnimationOnViews(R.anim.fade_in);

    new Handler().postDelayed(this::determineIfUserIsLoggedIn, 800);
  }

  private void determineIfUserIsLoggedIn() {
    messageRepository.getUserAuthentication().reloadCurrentUserAuthState(
        new OnTaskCompleteListener<Void>() {
          @Override
          public void onTaskSuccessful(Void result) {
            setUpWorkManagerRequest();
            openLandingActivity();
          }

          @Override
          public void onTaskCompleteButFailed(Void result) {
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

  private void setUpWorkManagerRequest() {
    Constraints constraints = new Constraints.Builder().setRequiresCharging(true).build();
    PeriodicWorkRequest saveRequest =
        new PeriodicWorkRequest.Builder(FireBaseSyncWorker.class, 1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build();

    WorkManager.getInstance(mContext).enqueue(saveRequest);
  }

  /**
   * Call to open RegistrationActivity from the current activity
   */
  private void openRegistrationActivity() {
    startAnimationOnViews(R.anim.fade_out);

    startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
    finish();
  }

  private void startAnimationOnViews(@AnimRes int animationId) {
    Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), animationId);
    tvAppVersion.startAnimation(animFadeOut);
    tvAppTitle.startAnimation(animFadeOut);
  }

  /**
   * Open LandingActivity and finish this one
   */
  private void openLandingActivity() {
    startAnimationOnViews(R.anim.fade_out);

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
    getWindow()
        .setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
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
