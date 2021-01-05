package com.fazemeright.chatbotmetcs622.ui.base

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {

    lateinit var context: Context

    protected lateinit var viewModel: T
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        viewModel = ViewModelProvider(this).get(viewModelClass::class.java)
        setContentView(layoutResId)
    }

    protected abstract val viewModelClass: T
    override fun setContentView(@LayoutRes layoutResId: Int) {
        super.setContentView(layoutResId)
        initViews()
        setListeners()
    }

    /**
     * Call to hide soft keyboard.
     *
     * @param activity calling activity
     */
    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        // Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        // If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Call to show keyboard.
     *
     * @param view view
     */
    fun showKeyBoard(view: View?) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * Call to disable the given button.
     *
     * @param button given button
     */
    protected fun disableButton(button: Button) {
        button.isEnabled = false
    }

    /**
     * Call to enable the given button.
     *
     * @param button given button
     */
    protected fun enableButton(button: Button) {
        button.isEnabled = true
    }

    /**
     * Template method to initialize views of activity.
     */
    open fun initViews() {}

    /**
     * Template method to set listeners of view or callback.
     */
    open fun setListeners() {}

    /**
     * To get layout resource id.
     */
    abstract val layoutResId: Int

    /**
     * Determine if network is available and connected.
     *
     * @return `true` if connected, else `false`
     */
    val isNetworkConnected: Boolean
        get() {
            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo? = cm.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        @MenuRes val menuId = menuId
        if (menuId != 0) {
            menuInflater.inflate(menuId, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Template method to get Menu Resource Id.
     *
     * @return `0` if no menu item needed to inflate, else `override`.
     */
    open val menuId: Int
        get() = 0
}