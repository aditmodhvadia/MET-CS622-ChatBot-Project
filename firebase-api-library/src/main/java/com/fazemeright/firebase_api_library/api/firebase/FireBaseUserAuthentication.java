package com.fazemeright.firebase_api_library.api.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.fazemeright.firebase_api_library.api.UserAuthResult;
import com.fazemeright.firebase_api_library.api.UserAuthentication;
import com.fazemeright.firebase_api_library.api.result.Result;
import com.fazemeright.firebase_api_library.listeners.OnCompleteListenerNew;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Objects;

public class FireBaseUserAuthentication implements UserAuthentication {
  private static FireBaseUserAuthentication mInstance;
  private final String TAG = FireBaseUserAuthentication.class.getSimpleName();

  public synchronized static FireBaseUserAuthentication getInstance() {
    if (mInstance == null) {
      mInstance = new FireBaseUserAuthentication();
    }
    return mInstance;
  }

  @Override
  public void signOutUser() {
    FirebaseAuth.getInstance().signOut();
  }

  @Override
  public boolean isUserVerified() {
    return FirebaseAuth.getInstance().getCurrentUser() != null;
  }

  @Override
  public void createNewUserWithEmailPassword(@NonNull String userEmail, @NonNull String password,
                                             @Nullable
                                                 OnCompleteListenerNew<UserAuthResult> onTaskCompleteListener) {
    Task<AuthResult> createUserTask =
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, password);
    if (onTaskCompleteListener != null) {
      createUserTask
          .addOnCompleteListener(new OnCompleteForOnTaskAuthResultAdapter(onTaskCompleteListener));
    }
  }

  @Override
  public void signInWithEmailAndPassword(@NonNull String userEmail, @NonNull String password,
                                         @Nullable
                                             OnCompleteListenerNew<UserAuthResult> onTaskCompleteListener) {
    Task<AuthResult> signInTask =
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, password);
    if (onTaskCompleteListener != null) {

      signInTask.addOnCompleteListener(
          new OnCompleteForOnTaskAuthResultAdapter(
              onTaskCompleteListener));
    }
  }

  @Override
  public void sendEmailVerification(
      @Nullable OnCompleteListenerNew<Void> onCompleteListener) {
    if (getCurrentUser() != null) {
      Task<Void> emailVerificationTask = getCurrentUser()
          .sendEmailVerification();
      if (onCompleteListener != null) {
        emailVerificationTask
            .addOnCompleteListener(new OnTaskCompleteAdapterForOnComplete<>(onCompleteListener));
      }
    }
  }

  @Nullable
  @Override
  public FirebaseUser getCurrentUser() {
    return FirebaseAuth.getInstance().getCurrentUser();
  }

  @Nullable
  @Override
  public String getUserName() {
    return Objects.requireNonNull(getCurrentUser()).getDisplayName();
  }

  @Nullable
  @Override
  public String getCurrentUserEmail() {
    if (getCurrentUser() != null) {
      return getCurrentUser().getEmail();
    }
    return null;
  }

  @Override
  public void sendPasswordResetEmail(@NonNull String userEmail,
                                     @Nullable OnCompleteListenerNew<Void> onCompleteListener) {
    Task<Void> sendEmailTask = FirebaseAuth.getInstance()
        .sendPasswordResetEmail(userEmail);
    if (onCompleteListener != null) {
      sendEmailTask
          .addOnCompleteListener(new OnTaskCompleteAdapterForOnComplete<>(onCompleteListener));
    }
  }

  @Override
  public void reloadCurrentUserAuthState(
      @Nullable OnCompleteListenerNew<Void> onTaskCompleteListener) {
    if (getCurrentUser() != null) {
      Task<Void> reloadUserTask = getCurrentUser()
          .reload();
      if (onTaskCompleteListener != null) {
        reloadUserTask.addOnCompleteListener(
            new OnTaskCompleteAdapterForOnComplete<>(onTaskCompleteListener));
      }
    } else {
      if (onTaskCompleteListener != null) {
        onTaskCompleteListener.onComplete(Result.exception(new Exception("User not logged in")));
      }
    }

  }

  @Override
  public @Nullable
  String getCurrentUserUid() {
    if (getCurrentUser() != null) {
      return getCurrentUser().getUid();
    }
    return null;
  }
}
