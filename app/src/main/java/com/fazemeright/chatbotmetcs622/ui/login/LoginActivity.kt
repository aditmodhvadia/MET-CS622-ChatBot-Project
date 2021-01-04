package com.fazemeright.chatbotmetcs622.ui.login

import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fazemeright.chatbotmetcs622.R
import com.fazemeright.chatbotmetcs622.intentservice.FireBaseIntentService
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity
import com.fazemeright.chatbotmetcs622.ui.landing.LandingActivity
import com.fazemeright.chatbotmetcs622.utils.AppUtils.isValidEmail
import com.fazemeright.chatbotmetcs622.utils.AppUtils.isValidPassword
import timber.log.Timber

class LoginActivity : BaseActivity<LoginActivityViewModel>(), View.OnClickListener {
    private var userEmailEditText: EditText? = null
    private var userPasswordEditText: EditText? = null
    private var tvDoNotHaveAccount: TextView? = null
    private var btnLogin: Button? = null
    override val viewModelClass: LoginActivityViewModel
        get() = LoginActivityViewModel(application)

    override fun initViews() {
        setUpSupportActionBar()
        userEmailEditText = findViewById(R.id.userLoginEmailEditText)
        userPasswordEditText = findViewById(R.id.userPasswordEditText)
        tvDoNotHaveAccount = findViewById(R.id.tvDontHaveAccount)
        btnLogin = findViewById(R.id.btnLogin)
        viewModel!!.userSignedIn.observe(this, {
            setLoginSuccessInButton()
            startMessageSyncWithCloud()
            openLandingActivity()
        })
    }

    /**
     * Sync messages of the cloud with local.
     */
    private fun startMessageSyncWithCloud() {
        val intent = Intent(context, FireBaseIntentService::class.java)
        intent.putExtra(
                FireBaseIntentService.ACTION_INTENT,
                FireBaseIntentService.Actions.ACTION_SYNC_MESSAGES)
        ContextCompat.startForegroundService(context!!, intent)
    }

    /**
     * Set login successful message in the login button.
     */
    private fun setLoginSuccessInButton() {
        btnLogin!!.text = getString(R.string.login_success_msg)
    }

    /**
     * Set up the support action bar.
     */
    private fun setUpSupportActionBar() {
        supportActionBar?.apply {
            title = getString(R.string.login_title)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setListeners() {
        btnLogin!!.setOnClickListener(this)
        tvDoNotHaveAccount!!.setOnClickListener(this)
    }

    override val layoutResId: Int
        get() = R.layout.activity_login

    override fun onClick(v: View) {
        val clickedViewId = v.id
        if (clickedViewId == R.id.tvDontHaveAccount) {
            goToRegistrationActivity()
        } else if (clickedViewId == R.id.btnLogin) {
            disableButton(btnLogin!!)
            performLogin(userEmailEditText!!.text.toString().trim { it <= ' ' },
                    userPasswordEditText!!.text.toString().trim { it <= ' ' })
            enableButton(btnLogin!!)
        }
    }

    /**
     * Perform login with the given credentials.
     *
     * @param email    user email address
     * @param password user password
     */
    private fun performLogin(email: String, password: String) {
        //    TODO: Move to ViewModel
        if (!isValidEmail(email)) {
            userEmailEditText!!.error = context!!.getString(R.string.incorrect_email_err_msg)
            userEmailEditText!!.requestFocus()
            return
        }
        if (!isValidPassword(password)) {
            userPasswordEditText!!.error = context!!.getString(R.string.incorrect_pass_err_msg)
            userPasswordEditText!!.requestFocus()
            return
        }
        Timber.i("Login clicked")
        viewModel!!.signInWithEmailPassword(
                email,
                password)
    }

    /**
     * Open LandingActivity and finish this one.
     */
    private fun openLandingActivity() {
        startActivity(Intent(this@LoginActivity, LandingActivity::class.java))
        finishAffinity()
    }

    /**
     * Go to Registration Activity.
     */
    private fun goToRegistrationActivity() {
        finish()
    }
}