package com.example.samsungtvcontrol;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.MediaRouter;

import com.example.samsungtvcontrol.tvcommand.SearchListener;
import com.example.samsungtvcontrol.utils.Constants;
import com.example.samsungtvcontrol.utils.Keycode;
import com.example.samsungtvcontrol.utils.SamsungWebsocket;
import com.example.samsungtvcontrol.utils.YoutubeService;
import com.samsung.multiscreen.Application;
import com.samsung.multiscreen.Search;
import com.samsung.multiscreen.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static Search search;
    private List<Map<String, String>> mDeviceInfos = new ArrayList<>();
    private SearchListener searchListener;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    EditText editText;
    String ip;
    String name;
    Button button;
    private String TAG = "Command-Samsung-Tv";
    private Service mService;
    private Application mApplication;
    private ArrayList mDeviceList = new ArrayList();
    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private MediaRouter.Callback mMediaRouterCallback;
    private SimpleAdapter mTVListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.sendText);
        button = findViewById(R.id.button);
        editText.setEnabled(false);
        editText.setClickable(false);
        editText.setOnClickListener(view -> promptSpeechInput());


        mTVListAdapter = new SimpleAdapter(
                this,
                mDeviceInfos,
                android.R.layout.simple_list_item_2,
                new String[]{"name", "type"},
                new int[]{android.R.id.text1, android.R.id.text2});
        button.setOnClickListener(v -> CreateSearchTvDialog()
        );

    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    final String text = result.get(0);
                    editText.setText(text);
                    editText.setGravity(Gravity.CENTER);
                    SamsungWebsocket samsungWebsocket = new SamsungWebsocket(ip, name);
                    samsungWebsocket.sendKey(Keycode.KEY_VOLUP.toString(), 1, "Click");
//                    samsungWebsocket.runApp(Constants.mapApp.get("Youtube"), Constants.DEEP_LINK, Constants.YOUTUBE_WATCH_PREFIX + "vTJdVE_gjI0");
//                    samsungWebsocket.sendKey(Keycode.KEY_ENTER.toString(), 1, "Click");
                    YoutubeService.openVideo(samsungWebsocket,"Di ve nha");

//                    OkHttpClient client = new OkHttpClient();
//                    Request request = new Request.Builder()
//                            .url("localhost:5000/execute-text").addHeader("Text", text)
//                            .build();
//                    try {
//                        Response response = client.newCall(request).execute();
//                        //todo viet them xu ly o day
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
                break;
            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy ");
        if (search != null) {
            search.stop();
            search = null;
        }
    }


    private void CreateSearchTvDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setIcon(R.drawable.ic_cast_white_24dp);
        alertBuilder.setTitle(R.string.connect_device);

        //MSF Search
        search = Service.search(getApplicationContext());
        search.start();

        // Start media router discovery
//        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
//                MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);


        Log.d(TAG, "menu_search : " + search);

        search.setOnServiceFoundListener(
                service -> {
                    Log.d(TAG, "Search.onFound() service : " + service.toString());
                    if (!mDeviceList.contains(service)) {
                        mDeviceList.add(service);
                        Map<String, String> device = new HashMap<>();
                        device.put("name", service.getName());
                        device.put("type", service.getType());
                        mDeviceInfos.add(device);
                        mTVListAdapter.notifyDataSetChanged();
                    }

                }
        );

        search.setOnServiceLostListener(
                service -> {
                    Log.d(TAG, "Search.onLost() service : " + service.toString());
                    mDeviceList.remove(service);
                    mDeviceInfos.remove(service.getName());
//                        mTVListAdapter.remove(service.getName());
                    mTVListAdapter.notifyDataSetChanged();
                }
        );

        alertBuilder.setAdapter(mTVListAdapter,
                (dialog, which) -> {
                    search.stop();
                    // End media router discovery
//                        mMediaRouter.removeCallback(mMediaRouterCallback);
                    //Save Service
                    String cName = mDeviceList.get(which).getClass().getName();
                    Service service = (Service) mDeviceList.get(which);
                    Uri uri = service.getUri();
                    ip = uri.getHost();
                    editText.setEnabled(true);
                    editText.setClickable(true);
                    name = service.getName();
                    System.out.println(ip + " XXXXXXXX " + name);
                    System.out.println(service.toString());
                    Log.d(TAG, "cName : " + cName);
                    Log.d(TAG, "mService : " + Service.class.getName());
                });

        alertBuilder.show();
    }
}