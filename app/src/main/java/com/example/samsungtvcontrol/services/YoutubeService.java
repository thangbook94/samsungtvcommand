package com.example.samsungtvcontrol.services;

import com.example.samsungtvcontrol.constants.Constant;
import com.example.samsungtvcontrol.constants.Keycode;
import com.example.samsungtvcontrol.entity.SamsungWebsocket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YoutubeService {

    public static void openVideo(SamsungWebsocket samsungWebsocket, String search) {
        List<String> result = getIdVideo(search);
        if (result.isEmpty() || result.size() == 0) {
            return;
        }
        samsungWebsocket.runApp(Constant.mapApp.get("Youtube"), Constant.DEEP_LINK, Constant.YOUTUBE_WATCH_PREFIX + result.get(0));
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        samsungWebsocket.sendKey(Keycode.KEY_ENTER.toString(), 1, "Click");
    }

    public static List<String> getIdVideo(String search) {
        List<String> re = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        String url = "https://youtube.googleapis.com/youtube/v3/search?q={{search}}&key=AIzaSyDN1u7faVHD78EN5bbkA4G_hvqzyptjRDk".replace("{{search}}", search);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println("Response: " + response.toString());
            if (response.isSuccessful() && response.body() != null) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = (JSONObject) jsonArray.get(i);
                    JSONObject itemId = (JSONObject) item.get("id");
                    String videoId = (String) itemId.get("videoId");
                    re.add(videoId);
                }
                return re;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return re;
    }
}
