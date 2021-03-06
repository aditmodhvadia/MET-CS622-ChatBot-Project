package com.fazemeright.library.api.domain.database

import com.fazemeright.library.api.Storable
import com.fazemeright.library.api.result.Result

interface DatabaseStore {
    suspend fun storeUserData(uid: String, userData: Storable): Result<Boolean>
    suspend fun storeMessage(storable: Storable, currentUserUid: String): Result<Boolean>
    suspend fun getAllMessagesForUser(currentUserUid: String): Result<List<Map<String, Any>>>?
}