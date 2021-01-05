package com.fazemeright.library.api.firebase

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fazemeright.library.api.domain.database.firebase.FireBaseDatabaseStore
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FireBaseDatabaseStoreTest {
    private var databaseStore: FireBaseDatabaseStore? = null
    @Before
    fun setUp() {
        databaseStore = FireBaseDatabaseStore.instance
        FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @After
    fun tearDown() {
        databaseStore = null
    }

    @Test
    @Ignore("Failed to initialize FirebaseApp before")
    fun generatesCorrectDocumentReferenceFromPath() {
        val documentReference = FirebaseFirestore.getInstance().collection(
                FireBaseDatabaseStore.BaseUrl.USERS).document("userid")
        Assert.assertEquals(documentReference, databaseStore!!.getDocumentReferenceFromPath(
                FireBaseDatabaseStore.BaseUrl.USERS + "/userid"))
    }
}