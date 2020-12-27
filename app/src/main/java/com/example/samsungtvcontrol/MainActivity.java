package com.example.samsungtvcontrol;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.samsungtvcontrol.tvcommand.SearchListener;
import com.example.samsungtvcontrol.utils.Constants;
import com.example.samsungtvcontrol.utils.Keycode;
import com.example.samsungtvcontrol.utils.SamsungWebsocket;
import com.example.samsungtvcontrol.utils.YoutubeService;
import com.samsung.multiscreen.Search;
import com.samsung.multiscreen.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static Search search;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    EditText editText;
    String ip;
    String name;
    Button button;
    Button number0, number1, number2, number3, number4, number5, number6, number7, number8, number9;
    Button buttonUp, buttonRigh, buttonLeft, buttonDown, buttonOk;
    Button power, source;
    Button netflix, youtube, browser;
    Button chup, chdown, volup, voldown;
    Button buttonback;
    SamsungWebsocket samsungWebsocket;
    SharedPreferences sharedPreferences;
    private List<Map<String, String>> mDeviceInfos = new ArrayList<>();
    private SearchListener searchListener;
    private String TAG = "Command-Samsung-Tv";
    private ArrayList mDeviceList = new ArrayList();
    private SimpleAdapter mTVListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        sharedPreferences = this.getSharedPreferences("tvSetting", Context.MODE_PRIVATE);
        String ipShared = sharedPreferences.getString("ip", null);
        System.out.println("IP: " + ip + " " + ipShared);
        String nameShared = sharedPreferences.getString("name", null);
        System.out.println("Name: " + name + " " + nameShared);
        if (ipShared != null) {
            ip = ipShared;
        }
        if (nameShared != null) {
            name = nameShared;
        }
        if (name != null && ip != null) {
            samsungWebsocket = new SamsungWebsocket(ip, name);
        }
        editText = findViewById(R.id.sendText);
        button = findViewById(R.id.button);
        editText.setEnabled(false);
        editText.setClickable(false);
        editText.setOnClickListener(view -> promptSpeechInput());
        number0 = findViewById(R.id.number0);
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        number4 = findViewById(R.id.number4);
        number5 = findViewById(R.id.number5);
        number6 = findViewById(R.id.number6);
        number7 = findViewById(R.id.number7);
        number8 = findViewById(R.id.number8);
        number9 = findViewById(R.id.number9);
        buttonUp = findViewById(R.id.buttonup);
        buttonDown = findViewById(R.id.buttondown);
        buttonLeft = findViewById(R.id.buttonleft);
        buttonRigh = findViewById(R.id.buttonright);
        buttonOk = findViewById(R.id.buttonok);
        power = findViewById(R.id.buttonpower);
        source = findViewById(R.id.buttonsource);
        netflix = findViewById(R.id.buttonnetflix);
        youtube = findViewById(R.id.buttonyoutube);
        browser = findViewById(R.id.buttonbrowser);
        chup = findViewById(R.id.channelup);
        chdown = findViewById(R.id.channeldown);
        voldown = findViewById(R.id.voldown);
        volup = findViewById(R.id.volup);
        buttonback = findViewById(R.id.buttonback);
        number0.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_0.toString(), 1, Constants.CLICK);
            }
        });
        number1.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_1.toString(), 1, Constants.CLICK);
            }
        });
        number2.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_2.toString(), 1, Constants.CLICK);
            }
        });
        number3.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_3.toString(), 1, Constants.CLICK);
            }
        });
        number4.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_4.toString(), 1, Constants.CLICK);
            }
        });
        number5.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_5.toString(), 1, Constants.CLICK);
            }
        });
        number6.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_6.toString(), 1, Constants.CLICK);
            }
        });
        number7.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_7.toString(), 1, Constants.CLICK);
            }
        });
        number8.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_8.toString(), 1, Constants.CLICK);
            }
        });
        number9.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_9.toString(), 1, Constants.CLICK);
            }
        });
        buttonUp.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_UP.toString(), 1, Constants.CLICK);
            }
        });
        buttonDown.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_DOWN.toString(), 1, Constants.CLICK);
            }
        });
        buttonLeft.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_LEFT.toString(), 1, Constants.CLICK);
            }
        });
        buttonRigh.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_RIGHT.toString(), 1, Constants.CLICK);
            }
        });
        buttonOk.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_ENTER.toString(), 1, Constants.CLICK);
            }
        });
        power.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_POWER.toString(), 1, Constants.CLICK);
            }
        });
        source.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_SOURCE.toString(), 1, Constants.CLICK);
            }
        });
        youtube.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.runApp(Constants.mapApp.get(Constants.YOUTUBE), Constants.DEEP_LINK, "");
            }
        });
        netflix.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.runApp(Constants.mapApp.get(Constants.NETFLIX), Constants.DEEP_LINK, "");
            }
        });
        browser.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.runApp(Constants.mapApp.get(Constants.BROWSER), Constants.DEEP_LINK, "");
            }
        });
        chup.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_CHUP.toString(), 1, Constants.CLICK);
            }
        });
        chdown.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_CHDOWN.toString(), 1, Constants.CLICK);
            }
        });
        volup.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_VOLUP.toString(), 1, Constants.CLICK);
            }
        });
        voldown.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_VOLDOWN.toString(), 1, Constants.CLICK);
            }
        });
        buttonback.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_RETURN.toString(), 1, Constants.CLICK);
            }
        });

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
//                    samsungWebsocket.runApp(Constants.mapApp.get("Youtube"), Constants.DEEP_LINK, Constants.YOUTUBE_WATCH_PREFIX + "vTJdVE_gjI0");
//                    samsungWebsocket.sendKey(Keycode.KEY_ENTER.toString(), 1, "Click");
                    YoutubeService.openVideo(samsungWebsocket, text);
//                    GoogleService.searchAndOpen(samsungWebsocket, "Di ve nha");
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
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("ip", ip);
                    editor.putString("name", name);
                    editor.apply();
                    samsungWebsocket = new SamsungWebsocket(ip, name);
                    System.out.println(service.toString());
                    Log.d(TAG, "cName : " + cName);
                    Log.d(TAG, "mService : " + Service.class.getName());
                });

        alertBuilder.show();
    }
}