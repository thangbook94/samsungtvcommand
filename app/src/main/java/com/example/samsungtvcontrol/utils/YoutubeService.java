package com.example.samsungtvcontrol.utils;

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
    public static List<String> getIdVideo(String search) {
        List<String> re = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://youtube-search.p.rapidapi.com/search?part=snippet&key=AIzaSyAOsteuaW5ifVvA_RkLXh0mYs6GLAD6ykc&q=cats")
                .get()
                .addHeader("x-rapidapi-key", "77e90bb7b3msh4c0571f20ed5065p1331d9jsnbb2e277d9c56")
                .addHeader("x-rapidapi-host", "youtube-search.p.rapidapi.com")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                if (jsonObject.has("results")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = (JSONObject) jsonArray.get(i);
                        JSONObject itemId = (JSONObject) item.get("id");
                        String videoId = (String) itemId.get("videoId");
                        re.add(videoId);
                    }
                    return re;
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return re;
    }
}
