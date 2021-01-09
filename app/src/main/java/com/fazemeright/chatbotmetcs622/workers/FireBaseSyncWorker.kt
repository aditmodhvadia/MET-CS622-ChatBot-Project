package com.fazemeright.chatbotmetcs622.workers

import android.app.Application
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fazemeright.chatbotmetcs622.domain.SyncLocalAndCloudMessagesUseCase

class FireBaseSyncWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    private val syncLocalAndCloudMessagesUseCase by lazy { SyncLocalAndCloudMessagesUseCase(context as Application) }

    override suspend fun doWork(): Result {
        syncLocalAndCloudMessagesUseCase()
        return Result.success()
    }
}