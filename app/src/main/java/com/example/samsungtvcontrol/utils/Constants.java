package com.example.samsungtvcontrol.utils;

import java.util.HashMap;

public class Constants {

    public static String NATIVE_LAUNCH = "NATIVE_LAUNCH";
    public static String DEEP_LINK = "DEEP_LINK";
    public static String SERVER = "localhost:5000/execute-text";
    public static String payloadControl = "{\"method\": \"ms.remote.control\", \"params\": {\"Cmd\": \"{{cmd}}\", \"DataOfCmd\": \"{{DataOfCmd}}\", \"Option\": \"false\", \"TypeOfRemote\": \"SendRemoteKey\"}}";
    public static String payloadRunapp = "{\"method\": \"ms.channel.emit\", \"params\": {\"event\": \"ed.apps.launch\", \"to\": \"host\", \"data\": {\"action_type\": \"{{action_type}}\", \"appId\": \"{{appId}}\", \"metaTag\": \"{{metaTag}}\"}}}";
    public static String payloadCursor = "{\"method\": \"ms.remote.control\", \"params\": {\"Cmd\": \"move\", \"Position\": {\"x\": {{x}}, \"y\": {{y}}, \"Time\": {{duration}}}, \"TypeOfRemote\": \"ProcessMouseDevice\"}}";
    public static String payloadAppList = "{\"method\": \"ms.channel.emit\", \"params\": {\"event\": \"ed.installedApp.get\", \"to\": \"host\"}}";
    public static String SSL_URI = "wss://{{host}}:8002/api/v2/channels/samsung.remote.control?name=U2Ftc3VuZ1R2UmVtb3Rl&token={{token}}";
    public static String URI = "ws://{host}:{port}/api/v2/channels/samsung.remote.control?name={name}";
    public static String REST_URL_FORMAT = "http://{{host}}:8001/api/v2/{{append}}";
    public static HashMap<String, String> mapApp = new HashMap<>();
    public static String YOUTUBE_WATCH_PREFIX = "vnd.youtube://";

    static {
        mapApp.put("Youtube", "111299001912");
        mapApp.put("Netflix", "11101200001");
        mapApp.put("Spotify", "3201606009684");
        mapApp.put("Prime Video", "3201512006785");
        mapApp.put("Browser", "org.tizen.browser");
    }
}
