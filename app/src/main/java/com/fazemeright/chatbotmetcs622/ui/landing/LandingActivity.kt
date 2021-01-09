package com.fazemeright.chatbotmetcs622.ui.landing

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fazemeright.chatbotmetcs622.R
import com.fazemeright.chatbotmetcs622.databinding.ActivityLandingBinding
import com.fazemeright.chatbotmetcs622.models.ChatRoom
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity
import com.fazemeright.chatbotmetcs622.ui.chat.ChatActivity
import com.fazemeright.chatbotmetcs622.ui.landing.ChatSelectionListAdapter.ChatListInteractionListener
import com.fazemeright.chatbotmetcs622.ui.registration.RegistrationActivity
import java.util.*

class LandingActivity : BaseActivity<LandingActivityViewModel, ActivityLandingBinding>(), ChatListInteractionListener {
    private lateinit var adapter: ChatSelectionListAdapter
    override val viewModelClass: LandingActivityViewModel
        get() = LandingActivityViewModel(application)

    override fun initViews() {
        supportActionBar?.apply {
            val firstName = viewModel.userName() ?: "Adit"
            title = getString(R.string.welcome_title) + " " + firstName
        }
        adapter = ChatSelectionListAdapter(this)
        setUpRecyclerView()
    }

    /**
     * Set up the RecyclerView.
     */
    private fun setUpRecyclerView() {
        binding.rvChatRoomList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                    DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            adapter = adapter
        }
        adapter.submitDataList(chatRoomList)
    }

    /**
     * Get the static chat room list.
     *
     * @return list of ChatRoom
     */
    private val chatRoomList: ArrayList<ChatRoom>
        get() {
            return ArrayList<ChatRoom>().apply {
                add(
                        ChatRoom(ChatRoom.BRUTE_FORCE_ID.toLong(), ChatRoom.BRUTE_FORCE, R.drawable.brute_force_logo))
                add(ChatRoom(ChatRoom.LUCENE_ID.toLong(), ChatRoom.LUCENE, R.drawable.lucene_logo))
                add(ChatRoom(ChatRoom.MONGO_DB_ID.toLong(), ChatRoom.MONGO_DB, R.drawable.mongodb_logo))
                add(ChatRoom(ChatRoom.MY_SQL_ID.toLong(), ChatRoom.MY_SQL, R.drawable.mysql_logo))
            }
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_landing, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            viewModel.logOutUser()
            openRegistrationActivity()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Navigate to RegistrationActivity.
     */
    private fun openRegistrationActivity() {
        startActivity(Intent(this@LandingActivity, RegistrationActivity::class.java))
        finish()
    }

    override fun onChatRoomClicked(chatRoom: ChatRoom) {
        Intent(this@LandingActivity, ChatActivity::class.java).apply {
            putExtra(SELECTED_CHAT_ROOM, chatRoom)
        }.also {
            startActivity(it)
        }
    }

    companion object {
        const val SELECTED_CHAT_ROOM = "chatRoomSelected"
    }

    override fun inflateLayoutFromBinding(): ActivityLandingBinding {
        return ActivityLandingBinding.inflate(layoutInflater)
    }
}