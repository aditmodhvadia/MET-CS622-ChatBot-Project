package com.fazemeright.chatbotmetcs622.ui.registration

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import com.fazemeright.chatbotmetcs622.R
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity
import com.fazemeright.chatbotmetcs622.ui.landing.LandingActivity
import com.fazemeright.chatbotmetcs622.ui.login.LoginActivity
import com.fazemeright.chatbotmetcs622.utils.AppUtils.arePasswordsValid
import com.fazemeright.chatbotmetcs622.utils.AppUtils.isValidEmail
import com.fazemeright.chatbotmetcs622.utils.AppUtils.isValidName
import com.fazemeright.chatbotmetcs622.utils.AppUtils.isValidPassword
import com.fazemeright.library.api.result.Result

class RegistrationActivity : BaseActivity<RegistrationActivityViewModel>(), View.OnClickListener {
    private var userEmailEditText: EditText? = null
    private var userPasswordEditText: EditText? = null
    private var userConPasswordEditText: EditText? = null
    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var tvHaveAccount: TextView? = null
    private var btnRegister: Button? = null
    override val viewModelClass: RegistrationActivityViewModel
        get() = RegistrationActivityViewModel(application)

    override fun initViews() {
        setUpSupportActionBar()
        userEmailEditText = findViewById(R.id.userLoginEmailEditText)
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        userPasswordEditText = findViewById(R.id.userPasswordEditText)
        userConPasswordEditText = findViewById(R.id.userConPasswordEditText)
        tvHaveAccount = findViewById(R.id.tvHaveAccount)
        btnRegister = findViewById(R.id.btnRegister)
        viewModel!!.userRegistered.observe(this, Observer { userRegistered: Result<Boolean> ->
            if (userRegistered.isSuccessful) {
                btnRegister?.text = getString(R.string.registration_success_msg)
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
        btnRegister?.setOnClickListener(this)
        tvHaveAccount?.setOnClickListener(this)
    }

    override val layoutResId: Int
        get() = R.layout.activity_registration

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.tvHaveAccount) {
            openLoginActivity()
        } else if (id == R.id.btnRegister) {
            disableButton(btnRegister!!)
            registerUser()
            enableButton(btnRegister!!)
        }
    }

    /**
     * Register the user.
     */
    private fun registerUser() {
        val email = userEmailEditText!!.text.toString()
        val firstName = etFirstName!!.text.toString()
        val lastName = etLastName!!.text.toString()
        val password = userPasswordEditText!!.text.toString()
        val conPassword = userConPasswordEditText!!.text.toString()
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
            userEmailEditText!!.error = context!!.getString(R.string.incorrect_email_err_msg)
            userEmailEditText!!.requestFocus()
            return
        }
        if (!isValidName(firstName)) {
            etFirstName!!.error = context!!.getString(R.string.incorrect_first_name)
            etFirstName!!.requestFocus()
            return
        }
        if (!isValidName(lastName)) {
            etLastName!!.error = context!!.getString(R.string.incorrect_last_name)
            etLastName!!.requestFocus()
            return
        }
        if (!isValidPassword(password)) {
            userPasswordEditText!!.error = context!!.getString(R.string.incorrect_pass_err_msg)
            userPasswordEditText!!.requestFocus()
            return
        }
        if (!arePasswordsValid(password, conPassword)) {
            userPasswordEditText!!.error = context!!.getString(R.string.passwords_dont_match_err_msg)
            userPasswordEditText!!.requestFocus()
            userPasswordEditText!!.setText("")
            userConPasswordEditText!!.setText("")
            return
        }
        viewModel!!.registerNewUser(
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
}