package com.fazemeright.firebase_api_library.api.firebase;

import androidx.annotation.NonNull;
import com.fazemeright.firebase_api_library.api.DatabaseStore;
import com.fazemeright.firebase_api_library.listeners.OnCompleteListenerNew;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.List;
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

//  public String joinPath(String... args) {
//    return TextUtils.join("/", args);
//  }

  @Override
  public void storeUserData(@NonNull String uid, @NonNull Map<String, Object> userData) {
    DocumentReference doc =
        FirebaseFirestore.getInstance().collection(BaseUrl.USERS).document(uid);
    writeData(doc, userData);
  }

  @Override
  public void storeMessage(@Nonnull Map<String, Object> messageHashMap,
                           String currentUserUid) {
    DocumentReference doc =
        FirebaseFirestore.getInstance().collection(BaseUrl.USERS).document(currentUserUid)
            .collection(BaseUrl.MESSAGES).document(String.valueOf(messageHashMap.get("mid")));
    writeData(doc, messageHashMap);
  }

  public void writeData(@NonNull DocumentReference documentReference,
                        @NonNull Map<String, Object> data) {
    documentReference.set(data);
  }

  public DocumentReference getDocumentReferenceFromPath(@NonNull String path) {
    return FirebaseFirestore.getInstance().document(path);
  }

  public void updateData(@NonNull String path, @NonNull Map<String, Object> data) {
    getDocumentReferenceFromPath(path).update(data);
  }

  @Override
  public void getAllMessagesForUser(@Nonnull String currentUserUid,
                                    OnCompleteListenerNew<List<HashMap<String, Object>>> messages) {

  }

  public static class BaseUrl {
    static final String USERS = "users";
    static final String MESSAGES = "messages";
    // Declare the constants over here
  }
}
