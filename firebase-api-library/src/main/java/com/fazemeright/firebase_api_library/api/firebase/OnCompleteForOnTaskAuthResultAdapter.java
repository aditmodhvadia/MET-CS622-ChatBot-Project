package com.fazemeright.firebase_api_library.api.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.fazemeright.firebase_api_library.api.UserAuthResult;
import com.fazemeright.firebase_api_library.api.result.Result;
import com.fazemeright.firebase_api_library.listeners.OnTaskCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import java.util.Objects;

public class OnCompleteForOnTaskAuthResultAdapter implements OnCompleteListener<AuthResult> {
  private final OnTaskCompleteListener<UserAuthResult> onTaskCompleteListener;

  public OnCompleteForOnTaskAuthResultAdapter(
      OnTaskCompleteListener<UserAuthResult> onTaskCompleteListener) {
    this.onTaskCompleteListener = onTaskCompleteListener;
  }

  @Override
  public void onComplete(@NonNull Task<AuthResult> task) {
    if (task.isSuccessful()) {
      this.onTaskCompleteListener
          .onComplete(Result.withData(new UserAuthResultAdapterForAuthResult(task.getResult())));
    } else {
      this.onTaskCompleteListener
          .onComplete(Result.exception(task.getException()));
    }
  }

  private static class UserAuthResultAdapterForAuthResult implements UserAuthResult {
    private final AuthResult authResult;

    UserAuthResultAdapterForAuthResult(AuthResult authResult) {
      this.authResult = authResult;
    }

    @Nullable
    @Override
    public String getUser() {
      return Objects.requireNonNull(this.authResult.getUser()).getDisplayName();
    }

    @Nullable
    @Override
    public String getErrorMsg() {
      // TODO: Parse error here
      return "Some error occurred, Change this later";
    }
  }

}