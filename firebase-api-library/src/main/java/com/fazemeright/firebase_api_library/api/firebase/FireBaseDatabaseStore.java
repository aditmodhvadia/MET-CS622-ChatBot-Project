package com.fazemeright.firebase_api_library.api.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.fazemeright.firebase_api_library.api.DatabaseStore;
import com.fazemeright.firebase_api_library.api.Storable;
import com.fazemeright.firebase_api_library.listeners.OnTaskCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

public class FireBaseDatabaseStore implements DatabaseStore {
  private static FireBaseDatabaseStore mInstance;

  /**
   * Get singleton instance of the object.
   *
   * @return instance
   */
  public static synchronized FireBaseDatabaseStore getInstance() {
    if (mInstance == null) {
      mInstance = new FireBaseDatabaseStore();
    }
    return mInstance;
  }

  @Override
  public void storeUserData(@NonNull String uid, @NonNull Storable userData) {
    DocumentReference doc =
        FirebaseFirestore.getInstance().collection(BaseUrl.USERS).document(uid);
    writeData(doc, userData);
  }

  @Override
  public void storeMessage(
      @Nonnull Storable object,
      String currentUserUid) {
    DocumentReference doc =
        FirebaseFirestore.getInstance().collection(BaseUrl.USERS).document(currentUserUid)
            .collection(BaseUrl.MESSAGES).document(String.valueOf(object.getId()));
    writeData(doc, object);
  }

  public void writeData(@NonNull DocumentReference documentReference,
                        @NonNull Storable object) {
    documentReference.set(object.getHashMap());
  }

  public DocumentReference getDocumentReferenceFromPath(@NonNull String path) {
    return FirebaseFirestore.getInstance().document(path);
  }

  public void updateData(@NonNull String path, @NonNull Storable object) {
    getDocumentReferenceFromPath(path).update(object.getHashMap());
  }

  @Override
  public void getAllMessagesForUser(@Nonnull String currentUserUid,
                                    @Nullable
                                        OnTaskCompleteListener<List<Map<String, Object>>>
                                        listener) {
    CollectionReference collectionReference =
        FirebaseFirestore.getInstance().collection(BaseUrl.USERS)
            .document(currentUserUid)
            .collection(BaseUrl.MESSAGES);
    readData(collectionReference, listener);
  }

  private void readData(@Nonnull CollectionReference collectionReference,
                        @Nullable OnTaskCompleteListener<List<Map<String, Object>>> listener) {
    collectionReference.get()
        .addOnCompleteListener(new OnCompleteListenerAdapterForFireBase(listener));
  }

  public static class BaseUrl {
    static final String USERS = "users";
    static final String MESSAGES = "messages";
    // Declare the constants over here
  }
}
