package com.fazemeright.firebase_api_library.api;

import androidx.annotation.Nullable;
import com.fazemeright.firebase_api_library.listeners.OnTaskCompleteListener;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

public interface DatabaseStore {

  void storeUserData(@Nonnull String uid, @Nonnull Map<String, Object> userData);

  void storeMessage(@Nonnull Map<String, Object> messageHashMap, String currentUserUid);

  void getAllMessagesForUser(@Nonnull String currentUserUid,
                             @Nullable
                                 OnTaskCompleteListener<List<Map<String, Object>>> listener);
}
