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
import java.util.concurrent.TimeUnit;
import timber.log.Timber;

public class SplashActivity extends BaseActivity<SplashActivityViewModel> {

  private TextView tvAppVersion;
  private TextView tvAppTitle;

  @Override
  protected Class<SplashActivityViewModel> getViewModelClass() {
    return SplashActivityViewModel.class;
  }

  @Override
  public void initViews() {
    hideSystemUi();
    tvAppVersion = findViewById(R.id.tvAppVersion);
    tvAppTitle = findViewById(R.id.tvAppTitle);

    tvAppVersion.setText(getAppVersion());

    startAnimationOnViews(R.anim.fade_in);

    new Handler().postDelayed(() -> viewModel.userAuthState.observe(this, result -> {
      if (result.isSuccessful()) {
        setUpWorkManagerRequest();
        openLandingActivity();
      } else {
        Timber.i("Open Registration Activity");
        openRegistrationActivity();
      }
    }), 400);
  }

  /**
   * Set up work manager request to sync message once daily.
   */
  private void setUpWorkManagerRequest() {
    Constraints constraints = new Constraints.Builder().setRequiresCharging(true).build();
    PeriodicWorkRequest saveRequest =
        new PeriodicWorkRequest.Builder(FireBaseSyncWorker.class, 1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build();

    WorkManager.getInstance(context).enqueue(saveRequest);
  }

  /**
   * Call to open RegistrationActivity from the current activity.
   */
  private void openRegistrationActivity() {
    startAnimationOnViews(R.anim.fade_out);

    startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
    finish();
  }

  /**
   * Run given animation on all the views.
   *
   * @param animationId animation resource id
   */
  private void startAnimationOnViews(@AnimRes int animationId) {
    Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), animationId);
    tvAppVersion.startAnimation(animFadeOut);
    tvAppTitle.startAnimation(animFadeOut);
  }

  /**
   * Open LandingActivity and finish this one.
   */
  private void openLandingActivity() {
    startAnimationOnViews(R.anim.fade_out);

    startActivity(new Intent(SplashActivity.this, LandingActivity.class));
    finish();
  }

  /**
   * Call to get the version of the Application.
   *
   * @return version name of the application
   */
  private String getAppVersion() {
    PackageInfo packageInfo;
    try {
      packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
      return packageInfo.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      return "beta-testing";
    }
  }

  /**
   * Makes the screen layout to cover the full display of the device.
   */
  private void hideSystemUi() {
    getWindow()
        .setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
  }

  @Override
  public int getLayoutResId() {
    return R.layout.activity_splash;
  }
}
