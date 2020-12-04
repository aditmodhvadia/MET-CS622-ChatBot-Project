package com.fazemeright.firebase_api_library.api;

import java.util.Map;
import javax.annotation.Nonnull;

public interface DatabaseStore {

  void storeUserData(@Nonnull String uid, @Nonnull Map<String, Object> userData);

  void writeData(@Nonnull String path, @Nonnull Map<String, Object> data);

  void updateData(@Nonnull String path, @Nonnull Map<String, Object> data);
}
