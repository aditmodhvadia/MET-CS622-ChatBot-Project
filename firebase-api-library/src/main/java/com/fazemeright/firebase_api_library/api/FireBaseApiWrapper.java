package com.fazemeright.firebase_api_library.api;

import androidx.annotation.NonNull;

import com.fazemeright.firebase_api_library.listeners.OnTaskCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/** Keep this as a Singleton Class */
abstract class FireBaseApiWrapper implements FireBaseApiWrapperInterface {

  //    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

  FirebaseFirestore db = FirebaseFirestore.getInstance();

  /** Signs out user */
  @Override
  public void signOutUser() {
    FirebaseAuth.getInstance().signOut();
  }

  @Override
  public void createNewUserWithEmailPassword(
      String userEmail, String password, OnCompleteListener<AuthResult> onCompleteListener) {
    FirebaseAuth.getInstance()
        .createUserWithEmailAndPassword(userEmail, password)
        .addOnCompleteListener(onCompleteListener);
  }

  @Override
  public void signInWithEmailAndPassword(
      @NonNull String userEmail,
      @NonNull String password,
      OnCompleteListener<AuthResult> onCompleteListener) {
    FirebaseAuth.getInstance()
        .signInWithEmailAndPassword(userEmail, password)
        .addOnCompleteListener(onCompleteListener);
  }

  @Override
  public String getCurrentUserEmail() {
    return getCurrentUser() != null ? getCurrentUser().getEmail() : null;
  }

  @Override
  public void sendEmailVerification(
      ActionCodeSettings actionCodeSettings,
      OnCompleteListener<Void> onCompleteListener,
      OnFailureListener onFailureListener) {
    if (getCurrentUser() != null) {
      getCurrentUser()
          .sendEmailVerification(actionCodeSettings)
          .addOnCompleteListener(onCompleteListener)
          .addOnFailureListener(onFailureListener);
    } else {
      onFailureListener.onFailure(new Exception("User not logged in"));
    }
  }

  @Override
  public void sendPasswordResetEmail(
      String userEmail,
      OnCompleteListener<Void> onCompleteListener,
      OnFailureListener onFailureListener) {
    FirebaseAuth.getInstance()
        .sendPasswordResetEmail(userEmail)
        .addOnCompleteListener(onCompleteListener)
        .addOnFailureListener(onFailureListener);
  }

  @Override
  public boolean isUserVerified() {
    return getCurrentUser() != null && getCurrentUser().isEmailVerified();
  }

  @Override
  public void reloadCurrentUserAuthState(
      final OnSuccessListener<Void> onSuccessListener, final OnFailureListener onFailureListener) {
    if (getCurrentUser() != null) {
      getCurrentUser()
          .reload()
          .addOnSuccessListener(onSuccessListener)
          .addOnFailureListener(onFailureListener);
    } else {
      onFailureListener.onFailure(new Exception("Some Error Occurred, Try again later"));
    }
  }

  void writeToFireStoreDocument(
      DocumentReference dr,
      Map<String, Object> map,
      final OnTaskCompleteListener onTaskCompleteListener) {
    dr.set(map)
        .addOnSuccessListener(
            new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                onTaskCompleteListener.onTaskSuccessful();
              }
            })
        .addOnFailureListener(
            new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                onTaskCompleteListener.onTaskFailed(e);
              }
            });
  }

  /**
   * Call to get current logged in user
   *
   * @return current logged in user
   */
  protected FirebaseUser getCurrentUser() {
    return FirebaseAuth.getInstance().getCurrentUser();
  }
}
