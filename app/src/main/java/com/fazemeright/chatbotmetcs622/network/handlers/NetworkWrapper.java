package com.fazemeright.chatbotmetcs622.network.handlers;

import android.content.Context;
import com.fazemeright.chatbotmetcs622.network.models.NetCompoundRes;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;

/*
 * Interface for calling API
 * */
public interface NetworkWrapper {

  /**
   * API GET method Request.
   *
   * @param context         App context
   * @param url             Request URL
   * @param typeToken       {@link TypeToken} of the expected parsed object
   * @param tag             request tag
   * @param networkCallback Network callback
   */
  <T> void makeGetRequest(
      Context context,
      String url,
      TypeToken<T> typeToken,
      String tag,
      NetworkCallback<T> networkCallback);

  /**
   * API POST method Request.
   *
   * @param context         App context
   * @param url             Request URL
   * @param typeToken       {@link TypeToken} of the expected parsed object
   * @param tag             request tag
   * @param networkCallback Network callback
   */
  <T> void makePostRequest(
      Context context,
      String url,
      TypeToken<T> typeToken,
      HashMap<String, String> hashMapHeader,
      String tag,
      NetworkCallback<T> networkCallback);

  /**
   * API POST method Request.
   *
   * @param context         App context
   * @param url             Request URL
   * @param dataObject      Request body
   * @param typeToken       {@link TypeToken} of the expected parsed object
   * @param tag             request tag
   * @param networkCallback Network callback
   */
  <T> void makePostRequest(
      Context context,
      String url,
      Object dataObject,
      TypeToken<T> typeToken,
      String tag,
      NetworkCallback<T> networkCallback);

  /**
   * API POST method Request.
   *
   * @param context         App context
   * @param url             Request URL
   * @param data            Request body
   * @param typeToken       {@link TypeToken} of the expected parsed object
   * @param tag             request tag
   * @param networkCallback Network callback
   */
  <T> void makePostStringRequest(
      Context context,
      String url,
      String data,
      TypeToken<T> typeToken,
      String tag,
      NetworkCallback<T> networkCallback);

  /**
   * API sync POST method Request. Expect a response/error in {@link NetCompoundRes}.
   *
   * @param context    App context
   * @param url        Request URL
   * @param dataObject Request body
   * @param typeToken  {@link TypeToken} of the expected parsed object
   * @param tag        request tag
   * @return Compound response ( It contains success / error ).
   */
  <T> NetCompoundRes<T> makePostRequestSync(
      Context context, String url, Object dataObject, TypeToken<T> typeToken, String tag);

  /**
   * API POST method Request with header.
   *
   * @param context         App context
   * @param url             Request URL
   * @param dataObject      Request body
   * @param typeToken       {@link TypeToken} of the expected parsed object
   * @param hashMapHeader   HashMap of request header
   * @param tag             request tag
   * @param networkCallback Network callback
   */
  <T> void makePostRequestHeader(
      Context context,
      String url,
      Object dataObject,
      TypeToken<T> typeToken,
      HashMap<String, String> hashMapHeader,
      String tag,
      NetworkCallback<T> networkCallback);

  /**
   * API POST method Request with header.
   *
   * @param context         App context
   * @param url             Request URL
   * @param typeToken       {@link TypeToken} of the expected parsed object
   * @param hashMapHeader   HashMap of request header
   * @param tag             request tag
   * @param networkCallback Network callback
   */
  <T> void makeDeleteRequestHeader(
      Context context,
      String url /*, Object dataObject*/,
      TypeToken<T> typeToken,
      HashMap<String, String> hashMapHeader,
      String tag,
      NetworkCallback<T> networkCallback);

  /**
   * API Get method Request with header.
   *
   * @param context         App context
   * @param url             Request URL
   * @param typeToken       {@link TypeToken} of the expected parsed object
   * @param hashMapHeader   HashMap of request header
   * @param tag             request tag
   * @param networkCallback Network callback
   */
  <T> void makeGetRequestHeader(
      Context context,
      String url,
      TypeToken<T> typeToken,
      HashMap<String, String> hashMapHeader,
      String tag,
      NetworkCallback<T> networkCallback);

  /**
   * API PUT Method Request with header.
   *
   * @param context         context
   * @param url             url
   * @param dataObject      object
   * @param typeToken       type
   * @param hashMapHeader   header
   * @param tag             tag
   * @param networkCallback callback
   * @param <T>             type
   */
  <T> void makePutRequestHeader(
      Context context,
      String url,
      Object dataObject,
      TypeToken<T> typeToken,
      HashMap<String, String> hashMapHeader,
      String tag,
      NetworkCallback<T> networkCallback);

  /**
   * Cancel api request by tag.
   *
   * @param tag request tag
   */
  void cancelRequest(String tag);

  <T> void makeCustomPostForSocialRequest(
      Context context,
      String url,
      String dataObject,
      TypeToken<T> typeToken,
      HashMap<String, String> hashMapAuthenticate,
      String s,
      NetworkCallback<T> networkCallback);
}
