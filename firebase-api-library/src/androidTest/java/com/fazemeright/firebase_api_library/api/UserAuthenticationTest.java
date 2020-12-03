package com.fazemeright.firebase_api_library.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.fazemeright.firebase_api_library.api.firebase.FireBaseUserAuthentication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserAuthenticationTest {
  public UserAuthentication userAuthentication;
  public final String EMAIL_ADDRESS = "test@test.com";
  public final String PASSWORD = "password123";

  @Before
  public void setUp() {
    userAuthentication = FireBaseUserAuthentication.getInstance();
  }

  @Test
  public void signOutUser() {
    assertNotNull(userAuthentication.getCurrentUserUid());
    userAuthentication.signOutUser();
    assertNull(userAuthentication.getCurrentUserUid());
  }

  @Test
  public void isUserVerified() {
  }

  @Test
  public void createNewUserWithEmailPassword() {
  }

  @Test
  public void signInWithEmailAndPassword() {
  }

  @Test
  public void sendEmailVerification() {
  }

  @Test
  public void getCurrentUserEmail() {
  }

  @Test
  public void sendPasswordResetEmail() {
  }

  @Test
  public void reloadCurrentUserAuthState() {
  }

  @Test
  public void getCurrentUserUid() {
  }

  @Test
  public void getCurrentUser() {
  }

  @Test
  public void getUserName() {
  }
}