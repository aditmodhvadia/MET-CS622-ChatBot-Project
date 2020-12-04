package com.fazemeright.firebase_api_library.api.firebase;

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
  public void writeData(String path, Map<String, Object> data) {
    DocumentReference documentReference = FirebaseFirestore.getInstance().document(path);
    documentReference.set(data);
  }

  @Override
  public void updateData(String path, Map<String, Object> data) {
    DocumentReference documentReference = FirebaseFirestore.getInstance().document(path);
    documentReference.update(data);
  }
}
