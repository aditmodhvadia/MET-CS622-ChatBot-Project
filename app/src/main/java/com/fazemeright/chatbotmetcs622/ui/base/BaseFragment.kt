package com.fazemeright.chatbotmetcs622.ui.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.fazemeright.chatbotmetcs622.network.ApiManager
import com.fazemeright.chatbotmetcs622.network.NetworkManager
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository

abstract class BaseFragment : Fragment() {
    lateinit var mContext: Context
    protected lateinit var messageRepository: MessageRepository
    protected lateinit var apiManager: ApiManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        messageRepository = MessageRepository.getInstance(mContext)
        apiManager = ApiManager.getInstance()
        apiManager.init(NetworkManager.getInstance())
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutResId, container, false)
        initViews(view)
        setListeners(view)
        return view
    }

    /**
     * To get layout resource id.
     */
    @get:LayoutRes
    abstract val layoutResId: Int

    /**
     * Template method to initialize views of activity.
     */
    fun initViews(view: View?) {}

    /**
     * Template method to set listeners of view or callback.
     *
     * @param view view
     */
    fun setListeners(view: View?) {}

    /**
     * Determine if network is connected.
     *
     * @return `true` if connected, else `false`
     */
    val isNetworkConnected: Boolean
        get() {
            val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo? = cm.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
}