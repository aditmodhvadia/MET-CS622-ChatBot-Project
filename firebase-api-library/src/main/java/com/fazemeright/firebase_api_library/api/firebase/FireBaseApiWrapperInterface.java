package com.fazemeright.firebase_api_library.api.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;

public interface FireBaseApiWrapperInterface {

  //    FireBase Auth functions
  void signOutUser();

  boolean isUserVerified();

  void createNewUserWithEmailPassword(
      String userEmail, String password, OnCompleteListener<AuthResult> onCompleteListener);

  void signInWithEmailAndPassword(
      String userEmail, String password, OnCompleteListener<AuthResult> onCompleteListener);

  void sendEmailVerification(
      ActionCodeSettings actionCodeSettings,
      OnCompleteListener<Void> onCompleteListener,
      OnFailureListener onFailureListener);

  String getCurrentUserEmail();

  void sendPasswordResetEmail(
      String userEmail,
      OnCompleteListener<Void> onCompleteListener,
      OnFailureListener onFailureListener);

  void reloadCurrentUserAuthState(
      OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener);
}
