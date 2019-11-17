package com.fazemeright.chatbotmetcs622.ui.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fazemeright.firebase_api_library.api.FireBaseApiManager;

public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    protected FireBaseApiManager apiManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        apiManager = FireBaseApiManager.getInstance();
        setContentView(getLayoutResId());
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initViews();
        setListeners();
    }

    /**
     * Call to disable the given button
     *
     * @param button given button
     */
    protected void disableButton(Button button) {
        button.setEnabled(false);
    }

    /**
     * Call to enable the given button
     *
     * @param button given button
     */
    protected void enableButton(Button button) {
        button.setEnabled(true);
    }

    /**
     * To initialize views of activity
     */
    public abstract void initViews();

    /**
     * To set listeners of view or callback
     */
    public abstract void setListeners();

    /**
     * To get layout resource id
     */
    public abstract int getLayoutResId();

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
