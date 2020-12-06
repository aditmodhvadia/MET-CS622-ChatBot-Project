package com.fazemeright.firebase_api_library.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.fazemeright.firebase_api_library.listeners.OnCompleteListenerNew;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;

public interface UserAuthentication {

  /**
   * Sign out the current user
   */
  void signOutUser();

  /**
   * Determine is user is authenticated or not
   *
   * @return <code>true</code> if user is authenticated, else <code>false</code>
   */
  boolean isUserVerified();

  /**
   * Create a new user
   *
   * @param userEmail              email address
   * @param password               password
   * @param onTaskCompleteListener task listener
   */
  void createNewUserWithEmailPassword(
      @NonNull String userEmail, @NonNull String password,
      @Nullable OnCompleteListenerNew<UserAuthResult> onTaskCompleteListener);

  void signInWithEmailAndPassword(
      @NonNull String userEmail, @NonNull String password, @Nullable
      OnCompleteListenerNew<UserAuthResult> onTaskCompleteListener);

  void sendEmailVerification(
      @Nullable OnCompleteListener<Void> onCompleteListener,
      @Nullable OnFailureListener onFailureListener);

  @Nullable
  String getCurrentUserEmail();

  void sendPasswordResetEmail(
      @NonNull String userEmail,
      @Nullable OnCompleteListener<Void> onCompleteListener,
      @Nullable OnFailureListener onFailureListener);

  void reloadCurrentUserAuthState(@Nullable OnCompleteListenerNew<Void> onTaskCompleteListener);

  @Nullable
  String getCurrentUserUid();

  @Nullable
  FirebaseUser getCurrentUser();

  @Nullable
  String getUserName();
}
