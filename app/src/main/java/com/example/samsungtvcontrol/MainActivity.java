package com.example.samsungtvcontrol;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.samsungtvcontrol.adapter.DeviceAdapter;
import com.example.samsungtvcontrol.constants.Constant;
import com.example.samsungtvcontrol.constants.Keycode;
import com.example.samsungtvcontrol.entity.SamsungWebsocket;
import com.example.samsungtvcontrol.entity.TvInfoDetail;
import com.example.samsungtvcontrol.services.GoogleService;
import com.example.samsungtvcontrol.services.YoutubeService;
import com.samsung.multiscreen.Search;
import com.samsung.multiscreen.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static Search search;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    TextView editText;
    String ip;
    String name;
    ImageButton refresh;
    ImageButton number0, number1, number2, number3, number4, number5, number6, number7, number8, number9;
    ImageButton buttonUp, buttonRight, buttonLeft, buttonDown, buttonOk;
    ImageButton power, source;
    ImageButton netflix, youtube, browser;
    ImageButton chup, chdown, volup, voldown;
    ImageButton buttonback, info;
    SamsungWebsocket samsungWebsocket;
    SharedPreferences sharedPreferences;
    private List<Map<String, String>> mDeviceInfos = new ArrayList<>();
    private final String TAG = "Command-Samsung-Tv";
    private ArrayList<Service> mDeviceList = new ArrayList<>();
    private ArrayList<TvInfoDetail> mInfoList = new ArrayList<>();
    private SimpleAdapter mTVListAdapter;
    Button custom1, custom2, custom3, custom4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        editText = findViewById(R.id.sendText);
        refresh = findViewById(R.id.button);
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
        buttonRight = findViewById(R.id.buttonright);
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
        info = findViewById(R.id.tvinfo);
        custom1 = findViewById(R.id.custom1);
        custom2 = findViewById(R.id.custom2);
        custom3 = findViewById(R.id.custom3);
        custom4 = findViewById(R.id.custom4);
        getFromSharedRef();
        info.setOnClickListener(view -> {
            AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
                    .setCancelable(false)
                    .setTitle("Device Info")
                    .setAdapter(new DeviceAdapter(mInfoList, MainActivity.this), null)
                    .setPositiveButton("OK", (dialog, id) -> {
                        dialog.cancel();
                    });
            ad.show();
        });
        number0.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_0.toString(), 1, Constant.CLICK);
            }
        });
        number1.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_1.toString(), 1, Constant.CLICK);
            }
        });
        number2.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_2.toString(), 1, Constant.CLICK);
            }
        });
        number3.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_3.toString(), 1, Constant.CLICK);
            }
        });
        number4.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_4.toString(), 1, Constant.CLICK);
            }
        });
        number5.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_5.toString(), 1, Constant.CLICK);
            }
        });
        number6.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_6.toString(), 1, Constant.CLICK);
            }
        });
        number7.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_7.toString(), 1, Constant.CLICK);
            }
        });
        number8.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_8.toString(), 1, Constant.CLICK);
            }
        });
        number9.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_9.toString(), 1, Constant.CLICK);
            }
        });
        buttonUp.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_UP.toString(), 1, Constant.CLICK);
            }
        });
        buttonDown.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_DOWN.toString(), 1, Constant.CLICK);
            }
        });
        buttonLeft.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_LEFT.toString(), 1, Constant.CLICK);
            }
        });
        buttonRight.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_RIGHT.toString(), 1, Constant.CLICK);
            }
        });
        buttonOk.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_ENTER.toString(), 1, Constant.CLICK);
            }
        });
        power.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_POWER.toString(), 1, Constant.CLICK);
            }
        });
        source.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_SOURCE.toString(), 1, Constant.CLICK);
            }
        });
        youtube.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.runApp(Constant.mapApp.get(Constant.YOUTUBE), Constant.DEEP_LINK, "");
            }
        });
        netflix.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.runApp(Constant.mapApp.get(Constant.NETFLIX), Constant.DEEP_LINK, "");
            }
        });
        browser.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.runApp(Constant.mapApp.get(Constant.BROWSER), Constant.DEEP_LINK, "");
            }
        });
        chup.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_CHUP.toString(), 1, Constant.CLICK);
            }
        });
        chdown.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_CHDOWN.toString(), 1, Constant.CLICK);
            }
        });
        volup.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_VOLUP.toString(), 1, Constant.CLICK);
            }
        });
        voldown.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_VOLDOWN.toString(), 1, Constant.CLICK);
            }
        });
        buttonback.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey(Keycode.KEY_RETURN.toString(), 1, Constant.CLICK);
            }
        });
        custom1.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey("KEY_" + custom1.getText().toString(), 1, Constant.CLICK);
            }
        });
        custom1.setOnLongClickListener(view -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
            builderSingle.setIcon(R.drawable.connect2);
            builderSingle.setTitle("Custom button");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.select_dialog_singlechoice);
            for (Keycode k : Keycode.values()) {
                if (k.toString().length() < 10) arrayAdapter.add(k.toString());
            }

            builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                String strName = arrayAdapter.getItem(which);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("custom1", strName);
                edit.apply();
                custom1.setText(strName.replace("KEY_", ""));

            });
            builderSingle.show();
            return true;
        });
        custom2.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey("KEY_" + custom2.getText().toString(), 1, Constant.CLICK);
            }
        });
        custom2.setOnLongClickListener(view -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
            builderSingle.setIcon(R.drawable.connect2);
            builderSingle.setTitle("Custom button");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.select_dialog_singlechoice);
            for (Keycode k : Keycode.values()) {
                if (k.toString().length() < 10) arrayAdapter.add(k.toString());
            }

            builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                String strName = arrayAdapter.getItem(which);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("custom2", strName);
                edit.apply();
                custom2.setText(strName.replace("KEY_", ""));

            });
            builderSingle.show();
            return true;
        });
        custom3.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey("KEY_" + custom3.getText().toString(), 1, Constant.CLICK);
            }
        });
        custom3.setOnLongClickListener(view -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
            builderSingle.setIcon(R.drawable.connect2);
            builderSingle.setTitle("Custom button");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.select_dialog_singlechoice);
            for (Keycode k : Keycode.values()) {
                if (k.toString().length() < 10) arrayAdapter.add(k.toString());
            }

            builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                String strName = arrayAdapter.getItem(which);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("custom1", strName);
                edit.apply();
                custom3.setText(strName.replace("KEY_", ""));

            });
            builderSingle.show();
            return true;
        });
        custom4.setOnClickListener(view -> {
            if (samsungWebsocket != null) {
                samsungWebsocket.sendKey("KEY_" + custom4.getText().toString(), 1, Constant.CLICK);
            }
        });
        custom4.setOnLongClickListener(view -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
            builderSingle.setIcon(R.drawable.connect2);
            builderSingle.setTitle("Custom button");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.select_dialog_singlechoice);
            for (Keycode k : Keycode.values()) {
                if (k.toString().length() < 10) arrayAdapter.add(k.toString());
            }

            builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                String strName = arrayAdapter.getItem(which);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("custom4", strName);
                edit.apply();
                custom4.setText(strName.replace("KEY_", ""));

            });
            builderSingle.show();
            return true;
        });

        mTVListAdapter = new SimpleAdapter(
                this,
                mDeviceInfos,
                android.R.layout.simple_list_item_2,
                new String[]{"name", "type"},
                new int[]{android.R.id.text1, android.R.id.text2});
        refresh.setOnClickListener(v -> {
                    ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (mWifi.isConnected()) {
                        CreateSearchTvDialog();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please connect wifi to use this feature", Toast.LENGTH_SHORT).show();
                    }

                }
        );
    }

    private void getFromSharedRef() {
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
        String c1 = sharedPreferences.getString("custom1", null);
        if (c1 != null) custom1.setText(c1.replace("KEY_", ""));
        String c2 = sharedPreferences.getString("custom2", null);
        if (c2 != null) custom2.setText(c2.replace("KEY_", ""));
        String c3 = sharedPreferences.getString("custom3", null);
        if (c3 != null) custom3.setText(c3.replace("KEY_", ""));
        String c4 = sharedPreferences.getString("custom4", null);
        if (c4 != null) custom4.setText(c4.replace("KEY_", ""));

        String ip = sharedPreferences.getString("ip", null);
        String name = sharedPreferences.getString("name", null);
        String version = sharedPreferences.getString("version", null);
        String uuid = sharedPreferences.getString("uuid", null);
        if (ip != null && name != null && version != null && uuid != null) {
            if (mInfoList == null || mDeviceList.size() == 0) {
                TvInfoDetail tvInfoDetail = TvInfoDetail.builder()
                        .uuid(uuid)
                        .ip(ip)
                        .version(version)
                        .name(name)
                        .build();
                mInfoList.add(tvInfoDetail);
            }
        }

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
                    String text = result.get(0);
//                    editText.setText(text);
//                    editText.setGravity(Gravity.CENTER);
                    int re = 2;
                    if (text.toLowerCase().contains("youtube")) {
                        re = 1;
                    } else if (text.contains("âm lượng")) {
                        re = 0;
                    } else if (text.contains("kênh")) {
                        re = 3;
                    }
                    text = text.replace("mở", "");
                    text = text.replace("Mở", "");
                    text = text.replace("Trên", "");
                    text = text.replace("Tìm kiếm", "");
                    text = text.replace("tìm kiếm", "");
                    processResponse(re, text);
//                    OkHttpClient client = new OkHttpClient();
//                    RequestBody rb = new FormBody.Builder().add("sentence", text).build();
//                    Request request = new Request.Builder()
//                            .url("http://192.168.1.179:5000/").post(rb)
//                            .build();
//                    try {
//                        Response response = client.newCall(request).execute();
//                        System.out.println("RESPONSE " + Objects.requireNonNull(response.body()).toString());
//                        JSONObject jsonObject = new JSONObject(response.body().string());
//                        int re = Integer.parseInt(jsonObject.getString("result"));
//                        String textRe = jsonObject.getString("text");
//                        processResponse(re, textRe);
//                        //todo viet them xu ly o day
//                    } catch (IOException | JSONException e) {
//                        e.printStackTrace();
//                    }
                }
                break;
            }

        }
    }

    private void processResponse(int label, String text) {
        System.out.println("XXXXXXXXXX begin process " + label + " " + text);
        switch (label) {
            case 0: {
                String timeString = text.replaceAll("\\D+", "");
                timeString = timeString.trim();
                int time = timeString.length() > 0 ? Integer.parseInt(timeString) : 1;
                if (text.toLowerCase().contains("tăng")) {
                    samsungWebsocket.sendKey(Keycode.KEY_VOLUP.toString(), time, Constant.CLICK);
                } else if (text.toLowerCase().contains("giảm")) {
                    samsungWebsocket.sendKey(Keycode.KEY_VOLDOWN.toString(), time, Constant.CLICK);
                }
                break;
            }
            case 1: {
                if (text.toLowerCase().equals("mở youtube")) {
                    samsungWebsocket.runApp(Constant.mapApp.get(Constant.YOUTUBE), Constant.DEEP_LINK, "");
                } else {
                    YoutubeService.openVideo(samsungWebsocket, text);
                }
                break;
            }
            case 2: {
                GoogleService.searchAndOpen(samsungWebsocket, text);
                break;
            }
            case 3: {
                String chString = text.replaceAll("\\D+", "");
                chString = chString.replaceAll("\\s+", "");
                int ch = chString.length() > 0 ? Integer.parseInt(chString) : -1;
                if (ch != -1) {
                    samsungWebsocket.openChannel(ch);
                }
                break;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFromSharedRef();
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
                (Service service) -> {
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
                    Service service = mDeviceList.get(which);
                    Uri uri = service.getUri();
                    ip = uri.getHost();
                    editText.setEnabled(true);
                    editText.setClickable(true);
                    name = service.getName();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("ip", ip);
                    editor.putString("name", name);
                    editor.putString("version", service.getVersion());
                    editor.putString("uuid", service.getId());
                    editor.putString("last_connect", new Timestamp(System.currentTimeMillis()).toString());
                    editor.apply();
                    mInfoList.add(TvInfoDetail.builder()
                            .name(service.getName())
                            .version(service.getVersion())
                            .uuid(service.getId())
                            .type(service.getType())
                            .build());
                    samsungWebsocket = new SamsungWebsocket(ip, name);
                    System.out.println(service.toString());
                    Log.d(TAG, "cName : " + cName);
                    Log.d(TAG, "mService : " + Service.class.getName());
                });

        alertBuilder.show();
    }
}