package com.fazemeright.firebase_api_library.api.firebase;

import androidx.annotation.NonNull;
import com.fazemeright.firebase_api_library.api.result.Result;
import com.fazemeright.firebase_api_library.listeners.OnTaskCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

public class OnCompleteListenerAdapterForFireBase implements
    OnCompleteListener<QuerySnapshot> {
  private final OnTaskCompleteListener<List<Map<String, Object>>> listener;

  public OnCompleteListenerAdapterForFireBase(
      @Nonnull OnTaskCompleteListener<List<Map<String, Object>>> listener) {
    this.listener = listener;
  }

  @Override
  public void onComplete(@NonNull Task<QuerySnapshot> task) {
    if (task.isSuccessful()) {
      List<Map<String, Object>> data = new ArrayList<>();
      for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
        data.add(documentSnapshot.getData());
      }
      listener.onComplete(Result.withData(data));
    } else {
      listener.onComplete(Result.exception(task.getException()));
    }
  }
}
