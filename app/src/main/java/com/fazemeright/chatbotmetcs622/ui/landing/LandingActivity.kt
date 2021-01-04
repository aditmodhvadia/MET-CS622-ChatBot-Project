package com.fazemeright.chatbotmetcs622.ui.landing

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fazemeright.chatbotmetcs622.R
import com.fazemeright.chatbotmetcs622.models.ChatRoom
import com.fazemeright.chatbotmetcs622.ui.base.BaseActivity
import com.fazemeright.chatbotmetcs622.ui.chat.ChatActivity
import com.fazemeright.chatbotmetcs622.ui.landing.ChatSelectionListAdapter.ChatListInteractionListener
import com.fazemeright.chatbotmetcs622.ui.registration.RegistrationActivity
import java.util.*

class LandingActivity : BaseActivity<LandingActivityViewModel>(), ChatListInteractionListener {
    private var rvChatRoomList: RecyclerView? = null
    private var adapter: ChatSelectionListAdapter? = null
    override val viewModelClass: LandingActivityViewModel
        get() = LandingActivityViewModel(application)

    override fun initViews() {
        supportActionBar?.apply {
            val firstName = viewModel?.userName ?: "Adit"
            title = getString(R.string.welcome_title) + " " + firstName
        }
        rvChatRoomList = findViewById(R.id.rvChatRoomList)
        adapter = ChatSelectionListAdapter(this)
        setUpRecyclerView()
    }

    /**
     * Set up the RecyclerView.
     */
    private fun setUpRecyclerView() {
        rvChatRoomList!!.setHasFixedSize(true)
        rvChatRoomList!!.layoutManager = LinearLayoutManager(context)
        rvChatRoomList!!.addItemDecoration(
                DividerItemDecoration(rvChatRoomList!!.context, LinearLayoutManager.VERTICAL))
        rvChatRoomList!!.adapter = adapter
        adapter!!.submitDataList(chatRoomList)
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
            viewModel!!.logOutUser()
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

    override val layoutResId: Int
        get() = R.layout.activity_landing

    override fun onChatRoomClicked(chatRoom: ChatRoom) {
        val intent = Intent(this@LandingActivity, ChatActivity::class.java)
        intent.putExtra(SELECTED_CHAT_ROOM, chatRoom)
        startActivity(intent)
    }

    companion object {
        const val SELECTED_CHAT_ROOM = "chatRoomSelected"
    }
}