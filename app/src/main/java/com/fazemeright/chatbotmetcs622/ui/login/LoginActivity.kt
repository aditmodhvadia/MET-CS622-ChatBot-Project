package com.fazemeright.chatbotmetcs622.ui.login

import android.content.Intent
import android.view.MenuItem
import android.view.View
import com.fazemeright.chatbotmetcs622.R
import com.fazemeright.chatbotmetcs622.databinding.ActivityLoginBinding
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity
import com.fazemeright.chatbotmetcs622.ui.landing.LandingActivity
import com.fazemeright.chatbotmetcs622.utils.AppUtils.isValidEmail
import com.fazemeright.chatbotmetcs622.utils.AppUtils.isValidPassword
import timber.log.Timber

class LoginActivity : BaseActivity<LoginActivityViewModel, ActivityLoginBinding>(), View.OnClickListener {
    override val viewModelClass: LoginActivityViewModel by lazy { LoginActivityViewModel(application) }

    override fun initViews() {
        setUpSupportActionBar()
        viewModel.userSignedIn.observe(this, {
            setLoginSuccessInButton()
            viewModel.syncMessagesWithLocalAndCloud()
            openLandingActivity()
        })
    }

    /**
     * Set login successful message in the login button.
     */
    private fun setLoginSuccessInButton() {
        binding.btnLogin.text = getString(R.string.login_success_msg)
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
        binding.btnLogin.setOnClickListener(this)
        binding.tvDontHaveAccount.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val clickedViewId = v.id
        if (clickedViewId == R.id.tvDontHaveAccount) {
            goToRegistrationActivity()
        } else if (clickedViewId == R.id.btnLogin) {
            disableButton(binding.btnLogin)
            performLogin(binding.userLoginEmailEditText.text.toString().trim(),
                    binding.userPasswordEditText.text.toString().trim())
            enableButton(binding.btnLogin)
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
            binding.userLoginEmailEditText.error = context.getString(R.string.incorrect_email_err_msg)
            binding.userLoginEmailEditText.requestFocus()
            return
        }
        if (!isValidPassword(password)) {
            binding.userLoginEmailEditText.error = context.getString(R.string.incorrect_pass_err_msg)
            binding.userLoginEmailEditText.requestFocus()
            return
        }
        Timber.i("Login clicked")
        viewModel.signInWithEmailPassword(
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

    override fun inflateLayoutFromBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }
}