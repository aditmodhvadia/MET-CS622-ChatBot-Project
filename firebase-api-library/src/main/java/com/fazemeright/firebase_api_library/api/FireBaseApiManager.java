package com.fazemeright.firebase_api_library.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fazemeright.firebase_api_library.listeners.DBValueListener;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FireBaseApiManager extends FireBaseApiWrapper {

    private static final String TAG = "FireBaseApiManager";
    private static FireBaseApiManager apiManager;

    public static FireBaseApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new FireBaseApiManager();
        }
        return apiManager;
    }

    public void logOutUser() {
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

    public void registerNewUserWithEmailPassword(final String userEmail, String password, final String firstName, final String lastName, final OnTaskCompleteListener onTaskCompleteListener) {
        createNewUserWithEmailPassword(userEmail, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> userProfile = new HashMap<>();
                    userProfile.put("emailAddress", userEmail);
                    userProfile.put("firstName", firstName);
                    userProfile.put("lastName", lastName);
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

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(firstName).build();

                    if (user != null) {
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User profile updated.");
                                        }
                                    }
                                });
                    }
                } else {
                    onTaskFailed(task.getException(), onTaskCompleteListener);
                }
            }
        });
    }

    private String getCurrentUserUid() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    public void logInWithEmailAndPassword(@NonNull String userEmail, @NonNull String password, final OnTaskCompleteListener onCompleteListener) {
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

    public void addMessageToUserDatabase(Map<String, Object> messageHashMap) {
        db.collection(BaseUrl.USERS)
                .document(getCurrentUserUid())
                .collection(BaseUrl.MESSAGES)
                .document(String.valueOf(messageHashMap.get("mid"))).set(messageHashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "onSuccess: Message written to database");
                    }
                });
    }

    public void syncMessages(final DBValueListener<List<Map<String, Object>>> listDBValueListener) {
        db.collection(BaseUrl.USERS)
                .document(getCurrentUserUid())
                .collection(BaseUrl.MESSAGES)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Map<String, Object>> hashMaps = new ArrayList<>();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                hashMaps.add(document.getData());

                            }
                            listDBValueListener.onDataReceived(hashMaps);
                        } else {
                            Log.e(TAG, "onComplete: \"Error getting documents" + task.getException());
                        }
                    }
                });
    }

    public String getCurrentUserFirstName() {
        return getCurrentUser().getDisplayName();
    }


    public static class BaseUrl {
        static final String USERS = "users";
        static final String MESSAGES = "messages";
        // Declare the constants over here
    }

}
