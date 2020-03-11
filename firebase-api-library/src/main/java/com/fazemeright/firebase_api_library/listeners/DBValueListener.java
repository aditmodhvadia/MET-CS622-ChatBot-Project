package com.fazemeright.firebase_api_library.listeners;

public interface DBValueListener<T> {

    void onDataReceived(T data);

    void onCancelled(Error error);
}
