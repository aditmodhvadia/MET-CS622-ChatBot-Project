package com.fazemeright.chatbotmetcs622.repositories.user

import android.content.Context
import com.example.network_library.retrofit.RetrofitApiManager
import com.fazemeright.chatbotmetcs622.database.ChatBotDatabase
import com.fazemeright.chatbotmetcs622.repositories.message.MessageRepositoryImpl
import com.fazemeright.library.api.Storable
import com.fazemeright.library.api.domain.authentication.UserAuthentication
import com.fazemeright.library.api.domain.authentication.firebase.FireBaseUserAuthentication
import com.fazemeright.library.api.domain.database.DatabaseStore
import com.fazemeright.library.api.domain.database.firebase.FireBaseDatabaseStore
import com.fazemeright.library.api.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
        private val database: ChatBotDatabase,
        private val apiManager: RetrofitApiManager,
        private val userAuthentication: UserAuthentication,
        private val onlineDatabaseStore: DatabaseStore
) : UserRepository {
    /**
     * Get user name of the current user.
     *
     * @return user name
     */
    override val userName: String?
        get() = userAuthentication.userName

    /**
     * Register new user and store the details.
     *
     * @param userEmail              email
     * @param password               password
     * @param firstName              first name
     * @param lastName               last name
     */
    override suspend fun createNewUserAndStoreDetails(userEmail: String, password: String, firstName: String, lastName: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                userAuthentication.createNewUserWithEmailPassword(
                        userEmail,
                        password).await()
                userAuthentication.currentUserUid?.let {
                    onlineDatabaseStore.storeUserData(
                            it,
                            getStorableFromUserDetails(userEmail, firstName, lastName))
                } ?: throw UnsupportedOperationException("User not logged in")
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    private fun getStorableFromUserDetails(userEmail: String, firstName: String,
                                           lastName: String): Storable {
        return object : Storable {
            override val hashMap: Map<String, Any>
                get() = mapOf(
                        "emailAddress" to userEmail,
                        "firstName" to firstName,
                        "lastName" to lastName
                )
            override val id: Long = 0
        }
    }


    /**
     * Call to logout user and clear all messages from Room.
     */
    override fun logOutUser() {
        userAuthentication.signOutUser()
    }

    /**
     * Sign in user with email and password.
     *
     * @param email    email
     * @param password password
     * @param listener task completion listener`
     */
    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                userAuthentication.signInWithEmailAndPassword(email, password).await().let {
                    Result.Success(true)
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    /**
     * Reload the current user authentication state.
     *
     */
    override suspend fun reloadCurrentUserAuthState(): Result<Boolean> {
        return try {
            userAuthentication.reloadCurrentUserAuthState()?.await()
                    ?: throw Exception("User not logged in")
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    }

    companion object {
        private var repository: UserRepositoryImpl? = null

        /**
         * Call to get instance of MessageRepository with the given context.
         *
         * @param context given context
         * @return synchronized call to get Instance of MessageRepository class
         */
        fun getInstance(context: Context): UserRepositoryImpl {
            if (repository == null) {
                synchronized(MessageRepositoryImpl::class.java) {
                    val database = ChatBotDatabase.getInstance(context)
                    repository = UserRepositoryImpl(database, RetrofitApiManager, FireBaseUserAuthentication,
                            FireBaseDatabaseStore)
                }
            }
            return repository!!
        }
    }
}