package com.fazemeright.chatbotmetcs622.network;

import android.content.Context;
import com.fazemeright.chatbotmetcs622.database.messages.Message;
import com.fazemeright.chatbotmetcs622.models.ChatRoom;
import com.fazemeright.chatbotmetcs622.network.handlers.NetworkCallback;
import com.fazemeright.chatbotmetcs622.network.handlers.NetworkWrapper;
import com.fazemeright.chatbotmetcs622.network.models.NetError;
import com.fazemeright.chatbotmetcs622.network.models.NetResponse;
import com.fazemeright.chatbotmetcs622.network.models.request.MessageQueryRequestModel;
import com.fazemeright.chatbotmetcs622.network.models.response.QueryResponseMessage;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

public class ApiManager {

  private static ApiManager apiManager = null;
  private NetworkManager networkManager;

  public static ApiManager getInstance() {
    synchronized (ApiManager.class) {
      if (apiManager == null) {
        apiManager = new ApiManager();
      }
      return apiManager;
    }
  }

  /**
   * Initialize base url,alias key and {@link NetworkWrapper} from application side, so that no need
   * to pass base url in every user related network call.
   *
   * @param networkManager network manager
   */
  public void init(NetworkManager networkManager) {
    this.networkManager = networkManager;
  }

  /**
   * Call Query API to backend to fetch results for the given Message query
   *
   * @param context         context
   * @param newMessage      given message query
   * @param networkCallback callback to listen to response
   */
  public void queryDatabase(
      Context context,
      Message newMessage,
      final NetworkCallback<QueryResponseMessage> networkCallback) {
    String url = BaseUrl.BASE_URL.concat(BaseUrl.BASE_APP_NAME)
        .concat(getServerEndPoint((int) newMessage.getChatRoomId()));

    MessageQueryRequestModel messageQuery = new MessageQueryRequestModel(newMessage.getMsg());

    TypeToken<QueryResponseMessage> typeToken = new TypeToken<QueryResponseMessage>() {
    };
    networkManager.makePostRequest(
        context,
        url,
        messageQuery,
        typeToken,
        "",
        new NetworkCallback<QueryResponseMessage>() {
          @Override
          public void onSuccess(NetResponse<QueryResponseMessage> response) {
            networkCallback.onSuccess(response);
          }

          @Override
          public void onError(NetError error) {
            networkCallback.onError(error);
          }
        });
  }

  @NotNull
  private String getServerEndPoint(int chatRoomId) {
    String serverEndPoint;
    switch (chatRoomId) {
      case ChatRoom.BRUTE_FORCE_ID:
        serverEndPoint = DatabaseUrl.BRUTE_FORCE;
        break;
      case ChatRoom.LUCENE_ID:
        serverEndPoint = DatabaseUrl.LUCENE;
        break;
      case ChatRoom.MONGO_DB_ID:
        serverEndPoint = DatabaseUrl.MONGO_DB;
        break;
      default:
        serverEndPoint = DatabaseUrl.MY_SQL;
        break;
    }
    return serverEndPoint;
  }

  /**
   * DatabaseUrl module Api sub url
   */
  static class DatabaseUrl {
    static final String MONGO_DB = "/mongodb";
    static final String LUCENE = "/lucene";
    static final String MY_SQL = "/mysql";
    static final String BRUTE_FORCE = "/bruteforce";
  }

  /**
   * baseURL model for all the BASE URL addresses
   */
  public static class BaseUrl {
    static String BASE_URL =
        "http://192.168.43.28:8080"; //  Update the url as per your local ip address
    static final String BASE_APP_NAME = "/MET_CS622_ChatBot_Backend_war_exploded";

    /**
     * Set the local IP address or the base url address for the server where the database back end
     * is hosted. Also include the port.
     *
     * @param hostAddress ip address
     */
    public static void setLocalIP(String hostAddress) {
      BASE_URL = hostAddress;
    }
  }
}
