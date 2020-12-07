package com.fazemeright.firebase_api_library.api;

import java.util.Map;
import javax.annotation.Nonnull;

public interface Storable {

  /**
   * Get map of the object
   *
   * @return map of data
   */
  @Nonnull
  Map<String, Object> getHashMap();

  /**
   * ID
   *
   * @return id
   */
  long getId();
}
