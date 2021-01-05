package com.fazemeright.library.api.domain.database.firebase

import com.fazemeright.library.api.Storable
import com.fazemeright.library.api.domain.database.DatabaseStore
import com.fazemeright.library.api.result.Result
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object FireBaseDatabaseStore : DatabaseStore {
    override suspend fun storeUserData(uid: String, userData: Storable): Result<Boolean> {
        return runInScope {
            FirebaseFirestore.getInstance().collection(BaseUrl.USERS).document(uid).let {
                writeData(it, userData)
            }
        }
    }

    private suspend fun runInScope(block: () -> Task<Void>): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                block().await()
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun storeMessage(
            storable: Storable,
            currentUserUid: String): Result<Boolean> {
        return runInScope {
            FirebaseFirestore.getInstance().collection(BaseUrl.USERS).document(currentUserUid)
                    .collection(BaseUrl.MESSAGES).document(storable.id.toString()).let {
                        writeData(it, storable)
                    }
        }
    }

    private fun writeData(documentReference: DocumentReference,
                          data: Storable): Task<Void> {
        return documentReference.set(data.hashMap)
    }

    fun getDocumentReferenceFromPath(path: String): DocumentReference {
        return FirebaseFirestore.getInstance().document(path)
    }

    fun updateData(path: String, data: Storable) {
        getDocumentReferenceFromPath(path).update(data.hashMap)
    }

    override suspend fun getAllMessagesForUser(currentUserUid: String): Result<List<Map<String, Any>>> {
        return withContext(Dispatchers.IO) {
            try {
                FirebaseFirestore.getInstance().collection(BaseUrl.USERS)
                        .document(currentUserUid)
                        .collection(BaseUrl.MESSAGES).let { collectionReference ->
                            readData(collectionReference).await()
                                    .let {
                                        Result.Success(it.documents.mapNotNull { documentSnapshot ->
                                            documentSnapshot.data?.toMap()
                                        })
                                    }
                        }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    }

    private fun readData(collectionReference: CollectionReference): Task<QuerySnapshot> {
        return collectionReference.get()
    }

    object BaseUrl {
        const val USERS = "users"
        const val MESSAGES = "messages" // Declare the constants over here
    }
}