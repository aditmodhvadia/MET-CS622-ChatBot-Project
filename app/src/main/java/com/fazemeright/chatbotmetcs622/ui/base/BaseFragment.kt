package com.fazemeright.chatbotmetcs622.ui.base

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository

abstract class BaseFragment : Fragment() {
    lateinit var mContext: Context
    protected lateinit var messageRepository: MessageRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireContext()
        messageRepository = MessageRepository.getInstance(mContext)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutResId, container, false)
        initViews()
        setListeners()
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
    fun initViews() {}

    /**
     * Template method to set listeners of view or callback.
     *
     * @param view view
     */
    private fun setListeners() {}

    /**
     * Determine if network is connected.
     *
     * @return `true` if connected, else `false`
     */
    val isNetworkConnected: Boolean
        get() {
            val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.isDefaultNetworkActive
        }
}