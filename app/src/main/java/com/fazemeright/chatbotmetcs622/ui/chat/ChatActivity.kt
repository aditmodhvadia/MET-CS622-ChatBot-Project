package com.fazemeright.chatbotmetcs622.ui.chat

import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.fazemeright.chatbotmetcs622.R
import com.fazemeright.chatbotmetcs622.database.message.Message
import com.fazemeright.chatbotmetcs622.database.message.Message.Companion.newMessage
import com.fazemeright.chatbotmetcs622.databinding.ActivityChatBinding
import com.fazemeright.chatbotmetcs622.models.ChatRoom
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity
import com.fazemeright.chatbotmetcs622.ui.landing.LandingActivity
import com.fazemeright.library.api.result.Result
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import timber.log.Timber
import java.util.*

class ChatActivity : BaseActivity<ChatActivityViewModel, ActivityChatBinding>(), View.OnClickListener {
    private lateinit var adapter: ChatListAdapter
    private lateinit var chatRoom: ChatRoom
    private lateinit var dataFilterChipGroup: ChipGroup
    override val viewModelClass: ChatActivityViewModel = ChatActivityViewModel(application)

    override fun initViews() {
        dataFilterChipGroup = findViewById(R.id.dataFilterChipGroup)
        if (intent != null) {
            chatRoom = intent.getSerializableExtra(LandingActivity.SELECTED_CHAT_ROOM) as ChatRoom
            setUpSupportActionBar()
        }
        setupFilterKeywords(resources.getStringArray(R.array.query_sample_selection))
        adapter = ChatListAdapter(context)
        setUpRecyclerView()
        viewModel.getMessagesForChatRoom(chatRoom).observe(this, { messages: List<Message?>? ->
            if (messages != null) {
                adapter.updateList(messages)
            } else {
                Timber.e("No messages found")
            }
        })
        viewModel.messageSent.observe(this, { result: Result<Boolean> ->
            when (result) {
                is Result.Success -> {
                    binding.etMsg.setText("")
                    binding.rvChatList.scrollToPosition(adapter.itemCount)
                }
                is Result.Error -> {
                    // TODO: Show error to the user
                    if (result.exception != null) {
                        Toast.makeText(context, result.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    /**
     * Set up the recyclerview.
     */
    private fun setUpRecyclerView() {
        binding.rvChatList.apply {
            adapter = adapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            // Show user the most recent messages, hence scroll to the top
            scrollToPosition(adapter!!.itemCount)
        }
    }

    /**
     * Set up the support action bar.
     */
    private fun setUpSupportActionBar() {
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = chatRoom.name
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
        dataFilterChipGroup.removeAllViews()
        if (dataFilters == null) {
            //      hide ChipGroup if list is empty
            dataFilterChipGroup.visibility = View.INVISIBLE
        } else {
            for (dataFilter in dataFilters) {
                //                create new chip and apply attributes
                val chip = getChip(dataFilter)
                dataFilterChipGroup.addView(chip) // add chip to ChipGroup
                chip.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
                    if (isChecked) {
                        binding.etMsg.apply {
                            requestFocus()
                            setText(buttonView.text.toString())
                            setSelection(buttonView.text.toString().length)
                            showKeyBoard(this)
                        }
                    }
                }
            }
            dataFilterChipGroup.visibility = View.VISIBLE
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
        viewModel.clearAllChatRoomMessages(chatRoom)
    }

    override fun setListeners() {
        binding.ivSendMsg.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.ivSendMsg) {
            sendMessageClicked()
        }
    }

    /**
     * User clicked send message. Show new message to user and pass it to repository.
     */
    private fun sendMessageClicked() {
        val msg = binding.etMsg.text.toString().trim()
        if (TextUtils.isEmpty(msg)) {
            return
        }
        newMessage(msg, Message.SENDER_USER, chatRoom.name, chatRoom.id).let {
            viewModel.sendNewMessage(context, it)
        }
    }

    override val menuId: Int
        get() = R.menu.menu_chat

    override fun inflateLayoutFromBinding(): ActivityChatBinding {
        return ActivityChatBinding.inflate(layoutInflater)
    }
}