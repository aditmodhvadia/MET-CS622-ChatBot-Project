package com.fazemeright.firebase_api_library.api;

import com.fazemeright.firebase_api_library.listeners.OnCompleteListenerNew;
import com.fazemeright.firebase_api_library.listeners.OnTaskCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.model.ObjectValue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

public interface DatabaseStore {

  void storeUserData(@Nonnull String uid, @Nonnull Map<String, Object> userData);

  void storeMessage(@Nonnull Map<String, Object> messageHashMap, String currentUserUid);

  void writeData(@Nonnull String path, @Nonnull Map<String, Object> data);

  void updateData(@Nonnull String path, @Nonnull Map<String, Object> data);

  void readData();

  void getAllMessagesForUser(@Nonnull String currentUserUid,
                             OnCompleteListenerNew<List<HashMap<String, Object>>> messages);
}
