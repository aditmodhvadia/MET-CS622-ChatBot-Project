package com.fazemeright.firebase_api_library.api.firebase;

import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import com.fazemeright.firebase_api_library.api.DatabaseStore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;
import javax.annotation.Nonnull;

public class FireBaseDatabaseStore implements DatabaseStore {
  private final String TAG = FireBaseDatabaseStore.class.getSimpleName();
  private static FireBaseDatabaseStore mInstance;

  public synchronized static FireBaseDatabaseStore getInstance() {
    if (mInstance == null) {
      mInstance = new FireBaseDatabaseStore();
    }
    return mInstance;
  }

  public String joinPath(String... args) {
    return TextUtils.join("/", args);
  }

  @Override
  public void storeUserData(@NonNull String uid, @NonNull Map<String, Object> userData) {
    writeData(BaseUrl.USERS + "/" + uid, userData);
  }

  @Override
  public void storeMessage(@Nonnull Map<String, Object> messageHashMap,
                           String currentUserUid) {
    writeData(joinPath(BaseUrl.USERS, currentUserUid, BaseUrl.MESSAGES,
        String.valueOf(messageHashMap.get("mid"))), messageHashMap);
  }

  @Override
  public void writeData(@NonNull String path, @NonNull Map<String, Object> data) {
    getDocumentReferenceFromPath(path).set(data);
  }

  public DocumentReference getDocumentReferenceFromPath(@NonNull String path) {
    return FirebaseFirestore.getInstance().document(path);
  }

  @Override
  public void updateData(@NonNull String path, @NonNull Map<String, Object> data) {
    getDocumentReferenceFromPath(path).update(data);
  }

  public static class BaseUrl {
    static final String USERS = "users";
    static final String MESSAGES = "messages";
    // Declare the constants over here
  }
}
