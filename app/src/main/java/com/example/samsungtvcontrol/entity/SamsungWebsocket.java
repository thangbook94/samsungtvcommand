package com.example.samsungtvcontrol.entity;

import android.util.Base64;
import android.util.Log;

import com.example.samsungtvcontrol.constants.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class SamsungWebsocket {
    WebSocket ws;
    String tokenFromServer;
    OkHttpClient okHttpClient;
    Request request;
    WebSocketListener webSocketListener;
    String host;
    String name;

    public SamsungWebsocket(String host, String name) {
        this.host = host;
        this.name = name;
        String URI = getUri(host, tokenFromServer);
        // Install the all-trusting trust manager
        // Create a trust manager that does not validate certificate chains
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain,
                                                       String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain,
                                                       String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true).build();
            request = new Request.Builder().url(URI).build();
            webSocketListener = new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    super.onOpen(webSocket, response);
                    Log.e("TV_COMMAND", "onOpen");
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    super.onMessage(webSocket, text);
                    try {
                        JSONObject jsonObject = new JSONObject(text);
                        if (jsonObject.has("data")) {
                            JSONObject data = (JSONObject) jsonObject.get("data");
                            if (data.has("token"))
                                tokenFromServer = (String) data.get("token");
                            System.out.println("Token: " + tokenFromServer);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onMessage(WebSocket webSocket, ByteString bytes) {
                    super.onMessage(webSocket, bytes);
                }

                @Override
                public void onClosing(WebSocket webSocket, int code, String reason) {
                    super.onClosing(webSocket, code, reason);
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    super.onClosed(webSocket, code, reason);
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                    super.onFailure(webSocket, t, response);
                }
            };
            ws = okHttpClient.newWebSocket(request, webSocketListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encodeBase64String(String text) {
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
    }

    public String getUri(String host, String token) {
        String uri = Constant.SSL_URI;
        uri = uri.replace("{{host}}", host);
//        uri = uri.replace("{{name}}", encodeBase64String(name));
        if (token == null)
            token = "";
        uri = uri.replace("{{token}}", token);
        return uri;
    }


    public void sendKey(String key, int times, String cmd) {
        cmd = cmd == null ? "Click" : cmd;
        String payload = Constant.payloadControl;
        payload = payload.replace("{{cmd}}", cmd);
        payload = payload.replace("{{DataOfCmd}}", key);
        for (int i = 0; i < times; i++) {
            ws.send(payload);
        }
    }

    public void holdKey(String key, int seconds) throws InterruptedException {
        sendKey(key, 1, "Press");
        Thread.sleep(seconds * 1000);
        sendKey(key, 1, "Release");
    }

    public void moveCursor(int x, int y, int duration) {
        String payload = Constant.payloadCursor;
        payload = payload.replace("{{x}}", x + "");
        payload = payload.replace("{{y}}", y + "");
        payload = payload.replace("{{duration}}", duration + "");
        ws.send(payload);
    }

    public void runApp(String appId, String appType, String metaTag) {
        appType = appType == null ? Constant.DEEP_LINK : appType;
        metaTag = metaTag == null ? "" : metaTag;
        String payload = Constant.payloadRunapp;
        payload = payload.replace("{{appId}}", appId);
        payload = payload.replace("{{action_type}}", appType);
        payload = payload.replace("{{metaTag}}", metaTag);
        ws.send(payload);
        ws = okHttpClient.newWebSocket(request, webSocketListener);
    }

    public void openBrowser(String url) {
        runApp(Constant.mapApp.get("Browser"), Constant.NATIVE_LAUNCH, url);
    }

    //chac la khong chay duoc
    public void appList() {
        String payload = Constant.payloadAppList;
        ws.send(payload);
    }
}
