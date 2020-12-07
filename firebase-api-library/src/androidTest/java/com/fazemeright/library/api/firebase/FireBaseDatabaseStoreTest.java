package com.fazemeright.library.api.firebase;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FireBaseDatabaseStoreTest {
  private FireBaseDatabaseStore databaseStore;

  @Before
  public void setUp() {
    databaseStore = FireBaseDatabaseStore.getInstance();
    FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().getTargetContext());
  }

  @After
  public void tearDown() {
    databaseStore = null;
  }

  @Test
  @Ignore("Failed to initialize FirebaseApp before")
  public void generatesCorrectDocumentReferenceFromPath() {
    DocumentReference documentReference = FirebaseFirestore.getInstance().collection(
        FireBaseDatabaseStore.BaseUrl.USERS).document("userid");
    assertEquals(documentReference, databaseStore.getDocumentReferenceFromPath(
        FireBaseDatabaseStore.BaseUrl.USERS + "/userid"));
  }

}