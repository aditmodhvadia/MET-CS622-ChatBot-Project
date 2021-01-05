package com.fazemeright.library.api.result

/*
class ResultAdapterForBooleanLiveUpdates<T>(
        private val mutableLiveData: MutableLiveData<Result<Boolean?>>) : OnTaskCompleteListener<T> {
    override fun onComplete(task: TaskResult<T>) {
        if (task.isSuccessful) {
            mutableLiveData.setValue(withData(true))
        } else {
            mutableLiveData.setValue(Result.exception(task.exception))
        }
    }
}*/
