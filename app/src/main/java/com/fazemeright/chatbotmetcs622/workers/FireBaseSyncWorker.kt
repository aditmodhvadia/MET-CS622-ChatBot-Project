package com.fazemeright.chatbotmetcs622.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fazemeright.chatbotmetcs622.repositories.MessageRepository

class FireBaseSyncWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        syncMessagesWithCloudAndLocal()
        return Result.success()
    }

    /**
     * Sync messages from local to cloud, and then from cloud to local.
     */
    private fun syncMessagesWithCloudAndLocal() {
        MessageRepository.getInstance(applicationContext)?.apply {
            addMessagesToFireBase(this.allMessages)
            syncMessagesFromFireStoreToRoom()
        }
    }
}