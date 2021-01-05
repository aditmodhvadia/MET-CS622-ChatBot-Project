package com.fazemeright.chatbotmetcs622.ui.registration

import android.content.Intent
import android.view.View
import com.fazemeright.chatbotmetcs622.R
import com.fazemeright.chatbotmetcs622.databinding.ActivityRegistrationBinding
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity
import com.fazemeright.chatbotmetcs622.ui.landing.LandingActivity
import com.fazemeright.chatbotmetcs622.ui.login.LoginActivity
import com.fazemeright.chatbotmetcs622.utils.AppUtils.arePasswordsValid
import com.fazemeright.chatbotmetcs622.utils.AppUtils.isValidEmail
import com.fazemeright.chatbotmetcs622.utils.AppUtils.isValidName
import com.fazemeright.chatbotmetcs622.utils.AppUtils.isValidPassword
import com.fazemeright.library.api.result.Result

class RegistrationActivity : BaseActivity<RegistrationActivityViewModel, ActivityRegistrationBinding>(), View.OnClickListener {
    override val viewModelClass: RegistrationActivityViewModel
        get() = RegistrationActivityViewModel(application)

    override fun initViews() {
        setUpSupportActionBar()

        viewModel.userRegistered.observe(this, { userRegistered: Result<Boolean> ->
            if (userRegistered is Result.Success) {
                binding.btnRegister.text = getString(R.string.registration_success_msg)
                openLandingActivity()
            }
        })
    }

    /**
     * Set up support action bar.
     */
    private fun setUpSupportActionBar() {
        supportActionBar?.apply {
            title = getString(R.string.registration)
        }
    }

    override fun setListeners() {
        binding.btnRegister.setOnClickListener(this)
        binding.tvHaveAccount.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.tvHaveAccount) {
            openLoginActivity()
        } else if (id == R.id.btnRegister) {
            disableButton(binding.btnRegister)
            registerUser()
            enableButton(binding.btnRegister)
        }
    }

    /**
     * Register the user.
     */
    private fun registerUser() {
        val email = binding.userLoginEmailEditText.text.toString()
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val password = binding.userPasswordEditText.text.toString()
        val conPassword = binding.userConPasswordEditText.text.toString()
        performRegistration(email, firstName, lastName, password, conPassword)
    }

    /**
     * Open Login Activity.
     */
    private fun openLoginActivity() {
        startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
    }

    /**
     * Call to perform validation on the input parameters and then perform registration.
     *
     * @param email       user email address
     * @param firstName   first name of user
     * @param lastName    last name of user
     * @param password    user selected password
     * @param conPassword user selected confirmation password
     */
    private fun performRegistration(
            email: String,
            firstName: String,
            lastName: String,
            password: String,
            conPassword: String) {
        // TODO: Move to ViewModel
        if (!isValidEmail(email)) {
            binding.userLoginEmailEditText.error = context.getString(R.string.incorrect_email_err_msg)
            binding.userLoginEmailEditText.requestFocus()
            return
        }
        if (!isValidName(firstName)) {
            binding.etFirstName.error = context.getString(R.string.incorrect_first_name)
            binding.etFirstName.requestFocus()
            return
        }
        if (!isValidName(lastName)) {
            binding.etLastName.error = context.getString(R.string.incorrect_last_name)
            binding.etLastName.requestFocus()
            return
        }
        if (!isValidPassword(password)) {
            binding.userPasswordEditText.error = context.getString(R.string.incorrect_pass_err_msg)
            binding.userPasswordEditText.requestFocus()
            return
        }
        if (!arePasswordsValid(password, conPassword)) {
            binding.userPasswordEditText.error = context.getString(R.string.passwords_dont_match_err_msg)
            binding.userPasswordEditText.requestFocus()
            binding.userPasswordEditText.setText("")
            binding.userConPasswordEditText.setText("")
            return
        }
        viewModel.registerNewUser(
                email,
                password,
                firstName,
                lastName)
    }

    /**
     * Open LandingActivity and finish this one.
     */
    private fun openLandingActivity() {
        startActivity(Intent(this@RegistrationActivity, LandingActivity::class.java))
        finish()
    }

    override fun inflateLayoutFromBinding(): ActivityRegistrationBinding {
        return ActivityRegistrationBinding.inflate(layoutInflater)
    }
}