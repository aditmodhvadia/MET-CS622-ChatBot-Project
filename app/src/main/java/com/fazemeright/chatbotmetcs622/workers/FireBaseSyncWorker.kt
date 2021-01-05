package com.fazemeright.chatbotmetcs622.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository

class FireBaseSyncWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        syncMessagesWithCloudAndLocal()
        return Result.success()
    }

    /**
     * Sync messages from local to cloud, and then from cloud to local.
     */
    private suspend fun syncMessagesWithCloudAndLocal() {
        MessageRepository.getInstance(applicationContext).apply {
            this.syncMessagesWithCloudAndLocal()
        }
    }
}