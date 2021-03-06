package com.fazemeright.chatbotmetcs622.ui.splash

import android.content.Intent
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.fazemeright.chatbotmetcs622.R
import com.fazemeright.chatbotmetcs622.databinding.ActivitySplashBinding
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity
import com.fazemeright.chatbotmetcs622.ui.landing.LandingActivity
import com.fazemeright.chatbotmetcs622.ui.registration.RegistrationActivity
import com.fazemeright.chatbotmetcs622.workers.FireBaseSyncWorker
import com.fazemeright.library.api.result.Result
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity<SplashActivityViewModel, ActivitySplashBinding>() {
    override val viewModelClass: SplashActivityViewModel by lazy { SplashActivityViewModel(application) }

    override fun initViews() {
        hideSystemUi()

        binding.tvAppVersion.text = appVersion
        startAnimationOnViews(R.anim.fade_in)
        Handler().postDelayed({
            Timber.d("Handler called")
            viewModel.userAuthState.observe(this, { result: Result<Boolean> ->
                Timber.d("$result")
                when (result) {
                    is Result.Success -> {
                        setUpWorkManagerRequest()
                        openLandingActivity()
                    }
                    else -> {
                        Timber.i("Open Registration Activity")
                        openRegistrationActivity()
                    }
                }
            })
        }, 400)
    }

    /**
     * Set up work manager request to sync message once daily.
     */
    private fun setUpWorkManagerRequest() {
        val constraints = Constraints.Builder().apply {
            setRequiresCharging(true)
        }
                .build()
        val saveRequest = PeriodicWorkRequest.Builder(FireBaseSyncWorker::class.java, 1, TimeUnit.DAYS).apply {
            setConstraints(constraints)
        }
                .build()
        WorkManager.getInstance(context).enqueue(saveRequest)
    }

    /**
     * Call to open RegistrationActivity from the current activity.
     */
    private fun openRegistrationActivity() {
        startAnimationOnViews(R.anim.fade_out)
        startActivity(Intent(this@SplashActivity, RegistrationActivity::class.java))
        finish()
    }

    /**
     * Run given animation on all the views.
     *
     * @param animationId animation resource id
     */
    private fun startAnimationOnViews(@AnimRes animationId: Int) {
        val animFadeOut = AnimationUtils.loadAnimation(applicationContext, animationId)
        binding.tvAppVersion.startAnimation(animFadeOut)
        binding.tvAppTitle.startAnimation(animFadeOut)
    }

    /**
     * Open LandingActivity and finish this one.
     */
    private fun openLandingActivity() {
        startAnimationOnViews(R.anim.fade_out)
        startActivity(Intent(this@SplashActivity, LandingActivity::class.java))
        finish()
    }

    /**
     * Call to get the version of the Application.
     *
     * @return version name of the application
     */
    private val appVersion: String
        get() {
            return try {
                packageManager.getPackageInfo(packageName, 0).versionName
            } catch (e: NameNotFoundException) {
                "beta-testing"
            }
        }

    /**
     * Makes the screen layout to cover the full display of the device.
     */
    private fun hideSystemUi() {
        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun inflateLayoutFromBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }
}