package com.fazemeright.firebase_api_library.api;

import java.util.Map;

public interface DatabaseStore {

  void writeData(String path, Map<String, Object> data);

  void updateData(String path, Map<String, Object> data);
}
