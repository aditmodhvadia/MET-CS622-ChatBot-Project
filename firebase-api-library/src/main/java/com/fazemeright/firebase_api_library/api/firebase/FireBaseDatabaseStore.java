package com.fazemeright.firebase_api_library.api.firebase;

import androidx.annotation.NonNull;
import com.fazemeright.firebase_api_library.api.DatabaseStore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;

public class FireBaseDatabaseStore implements DatabaseStore {
  private final String TAG = FireBaseDatabaseStore.class.getSimpleName();
  private static FireBaseDatabaseStore mInstance;

  public synchronized static FireBaseDatabaseStore getInstance() {
    if (mInstance == null) {
      mInstance = new FireBaseDatabaseStore();
    }
    return mInstance;
  }

  @Override
  public void storeUserData(@NonNull String uid, @NonNull Map<String, Object> userData) {
    writeData(BaseUrl.USERS + "/" + uid, userData);
  }

  @Override
  public void writeData(@NonNull String path, @NonNull Map<String, Object> data) {
    DocumentReference documentReference = FirebaseFirestore.getInstance().document(path);
    documentReference.set(data);
  }

  @Override
  public void updateData(@NonNull String path, @NonNull Map<String, Object> data) {
    DocumentReference documentReference = FirebaseFirestore.getInstance().document(path);
    documentReference.update(data);
  }

  public static class BaseUrl {
    static final String USERS = "users";
    static final String MESSAGES = "messages";
    // Declare the constants over here
  }
}
