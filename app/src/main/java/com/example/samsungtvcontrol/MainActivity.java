package com.example.samsungtvcontrol;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.MediaRouter;

import com.example.samsungtvcontrol.tvcommand.SearchListener;
import com.example.samsungtvcontrol.tvcommand.ServiceListAdapter;
import com.example.samsungtvcontrol.utils.Constants;
import com.example.samsungtvcontrol.utils.SamsungWebsocket;
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
        editText = (EditText) findViewById(R.id.sendText);
        button = findViewById(R.id.button);
        List<Service> listService = new ArrayList<>();
        ServiceListAdapter serviceListAdapter = new ServiceListAdapter(listService, this);
        editText.setOnClickListener(view -> promptSpeechInput());


        mTVListAdapter = new SimpleAdapter(
                this,
                mDeviceInfos,
                android.R.layout.simple_list_item_2,
                new String[]{"name", "type"},
                new int[]{android.R.id.text1, android.R.id.text2});
        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          CreateSearchTvDialog();
                                      }
                                  }
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
                    SamsungWebsocket samsungWebsocket = new SamsungWebsocket("192.168.1.122", "[Samsung tv 7 series]");
//                    samsungWebsocket.sendKey(Keycode.KEY_VOLUP.toString(), 1, "Click");
//                    samsungWebsocket.openBrowser("google.com.vn");
                    samsungWebsocket.runApp(Constants.mapApp.get("Youtube"), Constants.DEEP_LINK, Constants.YOUTUBE_WATCH_PREFIX + "PkSxlSsOAgw");
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


//    public boolean startDiscovery(SearchListener searchListener) {
//        if (!search.isSearching()) {
//            if (searchListener != null) {
//                this.searchListener = searchListener;
//                search.setOnStartListener(searchListener);
//                search.setOnStopListener(searchListener);
//            }
//            search.start();
//
//            startTimer(10000);
//
//        }
//        return false;
//    }

//    private void startTimer(long millis) {
//        new CountDownTimer(millis, 250) {
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//            }
//
//            @Override
//            public void onFinish() {
//                Log.d(TAG, "Timer finished. Call completeScan()");
//                cancel();
//               if (search != null) {
//            search.stop();
//            search = null;
//        }
//            }
//        }.start();
//    }

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

        // You can connect getByURI,getById
        // This is example code.
        /*
        Service.getByURI(Uri.parse("http://192.168.0.68:8001/api/v2/"), new Result<Service>() {
            @Override
            public void onSuccess(Service service) {
                mService = service;
                mApplication = mService.createApplication(mApplicationId, mChannelId);
                addAllListener(mApplication);
                mApplication.connect(new Result<Client>() {
                    @Override
                    public void onSuccess(Client client) {
                        mApplication.setDebug(true);
                        Log.d(TAG, "application.connect onSuccess " + client.toString());
                    }

                    @Override
                    public void onError(com.samsung.multiscreen.Error error) {
                        Log.d(TAG, "application.connect onError " + error.toString());
                        Toast.makeText(mContext, "Launch TV app error occurs : " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(Error error) {
                Log.d(TAG, "Service.getById  onError : ");
            }
        });

        Service.getById(mContext, "uuid:8304f069-639d-41a7-afcd-6bf8160dd88e", new Result<Service>() {
            @Override
            public void onSuccess(Service service) {
                mService = service;
                mApplication = mService.createApplication(mApplicationId, mChannelId);
                addAllListener(mApplication);
                mApplication.connect(new Result<Client>() {
                    @Override
                    public void onSuccess(Client client) {
                        mApplication.setDebug(true);
                        Log.d(TAG, "application.connect onSuccess " + client.toString());
                    }

                    @Override
                    public void onError(com.samsung.multiscreen.Error error) {
                        Log.d(TAG, "application.connect onError " + error.toString());
                        Toast.makeText(mContext, "Launch TV app error occurs : " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(Error error) {
                Log.d(TAG, "Service.getById  onError : ");
            }
        });
        */

        alertBuilder.setAdapter(mTVListAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        search.stop();
                        // End media router discovery
                        mMediaRouter.removeCallback(mMediaRouterCallback);
                        //Save Service
                        String cName = mDeviceList.get(which).getClass().getName();
                        Service service = (Service) mDeviceList.get(which);
                        Uri uri = service.getUri();
                        ip = uri.getHost();
                        editText.setEnabled(true);
                        name = service.getName();
                        Log.d(TAG, "cName : " + cName);
                        Log.d(TAG, "mService : " + Service.class.getName());
                    }
                });

        alertBuilder.show();
    }
}