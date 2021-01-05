package com.fazemeright.library.api.firebase

/*
class OnCompleteListenerAdapterForFireBase(
        private val listener: OnTaskCompleteListener<List<Map<String, Any>>?>?) : OnCompleteListener<QuerySnapshot> {
    override fun onComplete(task: Task<QuerySnapshot>) {
        if (task.isSuccessful) {
            val data: MutableList<Map<String, Any>> = ArrayList()
            for (documentSnapshot in task.result!!) {
                data.add(documentSnapshot.data)
            }
            listener?.onComplete(withData(data))
        } else {
            listener?.onComplete(Result.exception(task.exception))
        }
    }
}*/
