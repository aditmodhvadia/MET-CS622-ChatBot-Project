package com.fazemeright.firebase_api_library.listeners;

import androidx.annotation.NonNull;
import com.fazemeright.firebase_api_library.api.result.TaskResult;

/**
 * Functional Callback for all tasks.
 *
 * @param <T> Data type to be returned
 */
public interface OnTaskCompleteListener<T> {
  /**
   * SAM callback for completed tasks.
   *
   * @param task TaskResult which holds the result of the task
   */
  void onComplete(@NonNull TaskResult<T> task);
}


