package com.fazemeright.firebase_api_library.api;

import androidx.annotation.NonNull;

import com.fazemeright.firebase_api_library.listeners.OnTaskCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public void registerNewUserWithEmailPassword(final String userEmail, String password, final OnTaskCompleteListener onTaskCompleteListener) {
        createNewUserWithEmailPassword(userEmail, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                    TODO: Store userId and displayName in users collection under user id to FireStore
                    Map<String, Object> userProfile = new HashMap<>();
                    userProfile.put("emailAddress", userEmail);
                    DocumentReference dr = FirebaseFirestore.getInstance().collection(BaseUrl.USERS)
                            .document(getCurrentUserUid());

                    writeToFireStoreDocument(dr, userProfile, new OnTaskCompleteListener() {
                        @Override
                        public void onTaskSuccessful() {
                            onTaskCompleteListener.onTaskSuccessful();
                        }

                        @Override
                        public void onTaskCompleteButFailed(String errMsg) {
                            onTaskCompleteListener.onTaskCompleteButFailed(errMsg);
                        }

                        @Override
                        public void onTaskFailed(Exception e) {
                            onTaskCompleteListener.onTaskFailed(e);
                        }
                    });
                } else {
                    onTaskFailed(task.getException(), onTaskCompleteListener);
                }
            }
        });
    }

    private String getCurrentUserUid() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
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

    public void reloadUserAuthState(final OnTaskCompleteListener onTaskCompleteListener) {
        reloadCurrentUserAuthState(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onTaskCompleteListener.onTaskSuccessful();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onTaskCompleteListener.onTaskFailed(e);
            }
        });

    }

    public boolean isUserLoggedIn() {
        return getCurrentUserEmail() != null;
    }


    public static class BaseUrl {
        public static final String USERS = "users";
        // Declare the constants over here
    }

}
