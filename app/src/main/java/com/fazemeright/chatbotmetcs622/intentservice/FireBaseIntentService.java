package com.fazemeright.chatbotmetcs622.intentservice;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.fazemeright.chatbotmetcs622.database.messages.Message;
import com.fazemeright.firebase_api_library.api.FireBaseApiManager;

import java.util.HashMap;
import java.util.Map;

public class FireBaseIntentService extends BaseIntentService {

    public static final String ACTION_ADD_MESSAGE = "AddMessage";

    private FireBaseApiManager apiManager;

    @Override
    public int getWakeLockTimeoutInSeconds() {
        return 60;
    }

    @Override
    protected void onHandleNewIntent(Intent intent) {
        if (intent != null) {
            switch (intent.getStringExtra(ACTION)) {
                case ACTION_ADD_MESSAGE:
                    addMessageToFireStore((Message) intent.getSerializableExtra(MESSAGE));
                    break;
            }
        }
    }

    private void addMessageToFireStore(Message message) {
        Map<String, Object> messageHashMap = new HashMap<>();
        messageHashMap.put("mid", message.getMid());
        messageHashMap.put("msg", message.getMsg());
        messageHashMap.put("sender", message.getSender());
        messageHashMap.put("receiver", message.getReceiver());
        messageHashMap.put("chatRoomId", message.getChatRoomId());
        messageHashMap.put("timestamp", message.getTimestamp());
        apiManager.addMessageToUserDatabase(messageHashMap);
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        apiManager = FireBaseApiManager.getInstance();
    }
}
