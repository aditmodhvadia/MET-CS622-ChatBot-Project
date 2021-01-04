package com.fazemeright.chatbotmetcs622.ui.chat

import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fazemeright.chatbotmetcs622.R
import com.fazemeright.chatbotmetcs622.database.message.Message
import com.fazemeright.chatbotmetcs622.database.message.Message.Companion.newMessage
import com.fazemeright.chatbotmetcs622.models.ChatRoom
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity
import com.fazemeright.chatbotmetcs622.ui.landing.LandingActivity
import com.fazemeright.library.api.result.Result
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import timber.log.Timber
import java.util.*

class ChatActivity : BaseActivity<ChatActivityViewModel>(), View.OnClickListener {
    private var rvChatList: RecyclerView? = null
    private var adapter: ChatListAdapter? = null
    private var etMsg: EditText? = null
    private var ivSendMsg: ImageView? = null
    private lateinit var chatRoom: ChatRoom
    private var dataFilterChipGroup: ChipGroup? = null
    override val viewModelClass: ChatActivityViewModel
        get() = ChatActivityViewModel(application)

    override fun initViews() {
        etMsg = findViewById(R.id.etMsg)
        ivSendMsg = findViewById(R.id.ivSendMsg)
        rvChatList = findViewById(R.id.rvChatList)
        dataFilterChipGroup = findViewById(R.id.dataFilterChipGroup)
        if (intent != null) {
            chatRoom = intent.getSerializableExtra(LandingActivity.SELECTED_CHAT_ROOM) as ChatRoom
            setUpSupportActionBar()
        }
        setupFilterKeywords(resources.getStringArray(R.array.query_sample_selection))
        adapter = ChatListAdapter(context!!)
        setUpRecyclerView()
        viewModel!!.getMessagesForChatRoom(chatRoom).observe(this, { messages: List<Message?>? ->
            if (messages != null) {
                adapter!!.updateList(messages)
            } else {
                Timber.e("No messages found")
            }
        })
        viewModel!!.messageSent.observe(this, { result: Result<Boolean> ->
            if (result.isSuccessful) {
                etMsg?.setText("")
                rvChatList?.scrollToPosition(adapter!!.itemCount)
            } else {
                // TODO: Show error to the user
                if (result.exception != null) {
                    Toast.makeText(context, result.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    /**
     * Set up the recyclerview.
     */
    private fun setUpRecyclerView() {
        rvChatList!!.adapter = adapter
        rvChatList!!.layoutManager = linearLayoutManager
        rvChatList!!.setHasFixedSize(true)
        // Show user the most recent messages, hence scroll to the top
        rvChatList!!.scrollToPosition(adapter!!.itemCount)
    }

    /**
     * Set up the support action bar.
     */
    private fun setUpSupportActionBar() {
        if (supportActionBar != null) {
            supportActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            if (chatRoom != null) {
                supportActionBar!!.title = chatRoom!!.name
            }
        }
    }

    /**
     * Get LinearLayoutManager for the recyclerview.
     *
     * @return linear layout manager
     */
    private val linearLayoutManager: LinearLayoutManager
        get() {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.reverseLayout = true
            linearLayoutManager.stackFromEnd = true
            return linearLayoutManager
        }

    /**
     * Use to setup chips for the given list of data filter for device usage.
     *
     * @param dataFilters given array of data filter
     */
    private fun setupFilterKeywords(dataFilters: Array<String>?) {
        //        remove all views from ChipGroup if any
        dataFilterChipGroup!!.removeAllViews()
        if (dataFilters == null) {
            //      hide ChipGroup if list is empty
            dataFilterChipGroup!!.visibility = View.INVISIBLE
        } else {
            for (dataFilter in dataFilters) {
                //                create new chip and apply attributes
                val chip = getChip(dataFilter)
                dataFilterChipGroup!!.addView(chip) // add chip to ChipGroup
                chip.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
                    if (isChecked) {
                        etMsg!!.requestFocus()
                        etMsg!!.setText(buttonView.text.toString())
                        etMsg!!.setSelection(buttonView.text.toString().length)
                        showKeyBoard(etMsg)
                    }
                }
            }
            dataFilterChipGroup!!.visibility = View.VISIBLE
        }
    }

    private fun getChip(dataFilter: String): Chip {
        return object : Chip(Objects.requireNonNull(context)) {
            init {
                text = dataFilter
                isClickable = true
                isCloseIconVisible = false // close icon not required
                isCheckable = true // allow check changes, hence switch between chips
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == android.R.id.home) {
            onBackPressed()
        } else if (itemId == R.id.action_clear) {
            clearChatRoomMessagesClicked(chatRoom)
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Call to clear all message for the given ChatRoom.
     *
     * @param chatRoom given ChatRoom
     */
    private fun clearChatRoomMessagesClicked(chatRoom: ChatRoom) {
        viewModel!!.clearAllChatRoomMessages(chatRoom)
    }

    override fun setListeners() {
        ivSendMsg!!.setOnClickListener(this)
    }

    override val layoutResId: Int
        get() = R.layout.activity_chat

    override fun onClick(v: View) {
        if (v.id == R.id.ivSendMsg) {
            sendMessageClicked()
        }
    }

    /**
     * User clicked send message. Show new message to user and pass it to repository.
     */
    private fun sendMessageClicked() {
        val msg = etMsg!!.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(msg)) {
            return
        }
        val newMessage = newMessage(msg, Message.SENDER_USER, chatRoom.name, chatRoom.id)
        viewModel!!.sendNewMessage(context, newMessage)
    }

    override val menuId: Int
        get() = R.menu.menu_chat
}