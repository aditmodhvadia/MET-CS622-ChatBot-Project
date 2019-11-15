package com.fazemeright.firebase_api_library.api;

import androidx.annotation.NonNull;

import com.fazemeright.firebase_api_library.listeners.OnTaskCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class FireBaseApiManager extends FireBaseApiWrapper {

    private static FireBaseApiManager apiManager;

    public static FireBaseApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new FireBaseApiManager();
        }
        return apiManager;
    }

    public void forceSignOutUser() {
//        todo: log analytics event
        signOutUser();
    }

    /**
     * To determine whether user has verifies their email address or not
     *
     * @return boolean value for user email verification
     */
    public boolean isUserEmailVerified() {
        return isUserVerified();
    }

    public void createNewUserWithEmailPassword(String userEmail, String password, final OnTaskCompleteListener onTaskCompleteListener) {
        createNewUserWithEmailPassword(userEmail, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    onTaskCompleteListener.onTaskSuccessful();
                } else {
                    onTaskFailed(task.getException(), onTaskCompleteListener);
                }
            }
        });
    }

    public void signInWithEmailAndPassword(@NonNull String userEmail, @NonNull String password, final OnTaskCompleteListener onCompleteListener) {
        signInWithEmailAndPassword(userEmail, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    onCompleteListener.onTaskSuccessful();
                } else {
                    onTaskFailed(task.getException(), onCompleteListener);
                }
            }
        });
    }

    public String getCurrentLoggedInUserEmail() {
        return getCurrentUserEmail();
    }

    public void sendPasswordResetEmail(String userEmail, final OnTaskCompleteListener onTaskCompleteListener) {
        sendPasswordResetEmail(userEmail, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    onTaskCompleteListener.onTaskSuccessful();
                } else {
                    onTaskFailed(task.getException(), onTaskCompleteListener);
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onTaskCompleteListener.onTaskFailed(e);
            }
        });
    }

    private void onTaskFailed(Exception exception, OnTaskCompleteListener onTaskCompleteListener) {
        if (exception instanceof FirebaseNetworkException) {
            onTaskCompleteListener.onTaskCompleteButFailed("No Internet");
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            onTaskCompleteListener.onTaskCompleteButFailed("Email ID is already in use");
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            onTaskCompleteListener.onTaskCompleteButFailed("Invalid Credentials");
        } else {
            onTaskCompleteListener.onTaskCompleteButFailed("Error Occurred");
        }
    }

    public void reloadUserAuthState(OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        reloadCurrentUserAuthState(onSuccessListener, onFailureListener);
    }

    public boolean isUserLoggedIn() {
        return getCurrentUserEmail() != null;
    }


    public static class BaseUrl {
        // Declare the constants over here
    }

}
