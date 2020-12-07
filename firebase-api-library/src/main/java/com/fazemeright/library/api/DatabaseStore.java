package com.fazemeright.library.api;

import androidx.annotation.Nullable;
import com.fazemeright.library.listeners.OnTaskCompleteListener;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

public interface DatabaseStore {

  void storeUserData(@Nonnull String uid, @Nonnull Storable userData);

  void storeMessage(@Nonnull Storable object, String currentUserUid);

  void getAllMessagesForUser(@Nonnull String currentUserUid,
                             @Nullable
                                 OnTaskCompleteListener<List<Map<String, Object>>> listener);
}
