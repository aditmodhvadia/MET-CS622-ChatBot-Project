package com.fazemeright.library.api.domain.authentication.firebase.authresult

import android.app.Activity
import com.fazemeright.library.api.domain.authentication.UserAuthResult
import com.google.android.gms.tasks.*
import com.google.firebase.auth.AuthResult
import java.util.concurrent.Executor

class TaskAuthResultToUserAuthResultAdapter(private val task: Task<AuthResult>) : Task<UserAuthResult>() {

    private val successListeners: MutableList<OnSuccessListener<in UserAuthResult>> = mutableListOf()

    override fun isComplete() = task.isComplete


    override fun isSuccessful() = task.isSuccessful

    override fun isCanceled() = task.isCanceled

    override fun getResult(): UserAuthResult {
        return UserAuthResultAdapterForAuthResult(task.result)
    }

    override fun <X : Throwable?> getResult(p0: Class<X>): UserAuthResult {
        return UserAuthResultAdapterForAuthResult(task.result)
    }

    override fun getException() = task.exception

    override fun addOnSuccessListener(p0: OnSuccessListener<in UserAuthResult>): Task<UserAuthResult> {
        return this
    }

    override fun addOnSuccessListener(p0: Executor, p1: OnSuccessListener<in UserAuthResult>): Task<UserAuthResult> {
        return this
    }

    override fun addOnSuccessListener(p0: Activity, p1: OnSuccessListener<in UserAuthResult>): Task<UserAuthResult> {
        return this
    }

    override fun addOnFailureListener(p0: OnFailureListener): Task<UserAuthResult> {
        return this
    }

    override fun addOnFailureListener(p0: Executor, p1: OnFailureListener): Task<UserAuthResult> {
        return this
    }

    override fun addOnFailureListener(p0: Activity, p1: OnFailureListener): Task<UserAuthResult> {
        return this
    }

    override fun addOnCompleteListener(p0: OnCompleteListener<UserAuthResult>): Task<UserAuthResult> {
        return this
    }

    override fun addOnCompleteListener(p0: Executor, p1: OnCompleteListener<UserAuthResult>): Task<UserAuthResult> {
        return this
    }

    override fun addOnCompleteListener(p0: Activity, p1: OnCompleteListener<UserAuthResult>): Task<UserAuthResult> {
        return this
    }

    override fun addOnCanceledListener(p0: OnCanceledListener): Task<UserAuthResult> {
        return this
    }

    override fun addOnCanceledListener(p0: Executor, p1: OnCanceledListener): Task<UserAuthResult> {
        return this
    }

    override fun addOnCanceledListener(p0: Activity, p1: OnCanceledListener): Task<UserAuthResult> {
        return this
    }
}
