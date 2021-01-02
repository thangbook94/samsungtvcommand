package com.example.samsungtvcontrol.services;

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

public class GoogleService {
    public static void searchAndOpen(SamsungWebsocket samsungWebsocket, String search) {
        List<String> re = getResult(search);
        if (re.isEmpty() || re.size() == 0) {
            return;
        }
        samsungWebsocket.openBrowser(re.get(0));
    }

    public static List<String> getResult(String search) {
        List<String> re = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpClient client = new OkHttpClient();
        search = search.replace("\\s+", "+");
        String URL = "https://google-search3.p.rapidapi.com/api/v1/search/q={{SEARCH}}&num=10&lr=lang_vi";
        URL = URL.replace("{{SEARCH}}", search);
        Request request = new Request.Builder()
                .url(URL)
                .addHeader("x-rapidapi-key", "77e90bb7b3msh4c0571f20ed5065p1331d9jsnbb2e277d9c56")
                .addHeader("x-rapidapi-host", "google-search3.p.rapidapi.com")
                .addHeader("useQueryString", "true")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                if (jsonObject.has("results")) {
                    JSONArray linkDatas = jsonObject.getJSONArray("results");
                    for (int i = 0; i < linkDatas.length(); i++) {
                        JSONObject linkData = (JSONObject) linkDatas.get(0);
                        if (linkData.has("link")) {
                            String link = (String) linkData.get("link");
                            re.add(link);
                        }
                    }
                }
                return re;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return re;
    }
}
