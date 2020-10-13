package com.shorts.shortmaker;

import com.shorts.shortmaker.Actions.Action;
import com.shorts.shortmaker.Actions.ActionAlarmClock;
import com.shorts.shortmaker.Actions.ActionBluetooth;
import com.shorts.shortmaker.Actions.ActionBrightness;
import com.shorts.shortmaker.Actions.ActionChangeOrientation;
import com.shorts.shortmaker.Actions.ActionGmail;
import com.shorts.shortmaker.Actions.ActionGoogleMaps;
import com.shorts.shortmaker.Actions.ActionGps;
import com.shorts.shortmaker.Actions.ActionOpenFlash;
import com.shorts.shortmaker.Actions.ActionPhoneCall;
import com.shorts.shortmaker.Actions.ActionReadAuto;
import com.shorts.shortmaker.Actions.ActionSendTextMessage;
import com.shorts.shortmaker.Actions.ActionSetTimer;
import com.shorts.shortmaker.Actions.ActionSoundSettings;
import com.shorts.shortmaker.Actions.ActionSpotify;
import com.shorts.shortmaker.Actions.ActionWaze;
import com.shorts.shortmaker.DataClasses.ActionData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class ActionFactory {


    public enum Conditions {
        ON_DEFAULT,
        ON_AT_LOCATION
    }

    public static final String WHEN_CLICKING_ON_A_SHORTCUT = "On shortcut click";
    public static final String ON_LOCATION = "When entering a location";

    public static final HashMap<Conditions, String> ENUM_TO_CONDITION_TITLE = new HashMap<Conditions, String>() {{
        put(Conditions.ON_DEFAULT, WHEN_CLICKING_ON_A_SHORTCUT);
        put(Conditions.ON_AT_LOCATION, ON_LOCATION);
    }};

    public static final HashMap<String, Conditions> CONDITION_TITLE_TO_ENUM = new HashMap<String, Conditions>() {{
        put(WHEN_CLICKING_ON_A_SHORTCUT, Conditions.ON_DEFAULT);
        put(ON_LOCATION, Conditions.ON_AT_LOCATION);
    }};


    public static final String WAZE_ACTION_TITLE = "Waze";
    public static final String SPOTIFY_ACTION_TITLE = "Spotify";
    public static final String ALARM_CLOCK_ACTION_TITLE = "Alaram clock";
    public static final String TEXT_MESSAGE_ACTION_TITLE = "Text message";
    public static final String SOUND_MODE_ACTION_TITLE = "Sound mode";
    public static final String PHONE_CALL_ACTION_TITLE = "Phone call";
    public static final String TIMER_ACTION_TITLE = "Timer";
    public static final String BRIGHTNESS_ACTION_TITLE = "Brightness";
    public static final String ORIENTATION_ACTION_TITLE = "Orientation";
    public static final String FLASH_ACTION_TITLE = "Flashlight";
    public static final String GMAIL_ACTION_TITLE = "Gmail";
    public static final String BLUETOOTH_ACTION_TITLE = "Bluetooth";
    public static final String READ_AUTO_ACTION_TITLE = "Read auto";
    public static final String GOOGLE_MAPS_ACTION_TITLE = "Google maps";
    public static final String GPS_ACTION_TITLE = "Gps";


    public static final HashMap<String, Integer> ICON_NAME_TO_PATH = new HashMap<String, Integer>() {{
        put(WAZE_ACTION_TITLE, R.drawable.waze_icon);
        put(SPOTIFY_ACTION_TITLE, R.drawable.spotify_icon);
        put(ALARM_CLOCK_ACTION_TITLE, R.drawable.alarm_clock_icon);
        put(TEXT_MESSAGE_ACTION_TITLE, R.drawable.text_message);
        put(SOUND_MODE_ACTION_TITLE, R.drawable.sound_mode_icon);
        put(TIMER_ACTION_TITLE, R.drawable.timer_icon);
        put(PHONE_CALL_ACTION_TITLE, R.drawable.phone_icon);
        put(BRIGHTNESS_ACTION_TITLE, R.drawable.brightness_icon);
        put(ORIENTATION_ACTION_TITLE, R.drawable.orientation_icon);
        put(FLASH_ACTION_TITLE, R.drawable.flash_icon);
        put(GMAIL_ACTION_TITLE, R.drawable.gmail_icon);
        put(BLUETOOTH_ACTION_TITLE, R.drawable.bluetooth_icon);
        put(READ_AUTO_ACTION_TITLE, R.drawable.read_auto_icon);
        put(GOOGLE_MAPS_ACTION_TITLE, R.drawable.google_maps_icon);
        put(GPS_ACTION_TITLE, R.drawable.gps_icon);

    }};

    public static final ArrayList<ActionData> ACTION_DATA_ARRAY_LIST = new ArrayList<ActionData>(
            Arrays.asList(
                    new ActionData(ActionFactory.WAZE_ACTION_TITLE),
                    new ActionData(ActionFactory.SPOTIFY_ACTION_TITLE),
                    new ActionData(ActionFactory.ALARM_CLOCK_ACTION_TITLE),
                    new ActionData(ActionFactory.PHONE_CALL_ACTION_TITLE),
                    new ActionData(ActionFactory.TIMER_ACTION_TITLE),
                    new ActionData(ActionFactory.TEXT_MESSAGE_ACTION_TITLE),
                    new ActionData(ActionFactory.SOUND_MODE_ACTION_TITLE),
                    new ActionData(ActionFactory.BRIGHTNESS_ACTION_TITLE),
                    new ActionData(ActionFactory.ORIENTATION_ACTION_TITLE),
                    new ActionData(ActionFactory.FLASH_ACTION_TITLE),
                    new ActionData(ActionFactory.GMAIL_ACTION_TITLE),
                    new ActionData(ActionFactory.BLUETOOTH_ACTION_TITLE),
                    new ActionData(ActionFactory.READ_AUTO_ACTION_TITLE),
                    new ActionData(ActionFactory.GOOGLE_MAPS_ACTION_TITLE),
                    new ActionData(ActionFactory.GPS_ACTION_TITLE)

            ));


    public static Action getAction(String actionTitle) {
        switch (actionTitle) {
            case WAZE_ACTION_TITLE:
                return new ActionWaze();
            case SPOTIFY_ACTION_TITLE:
                return new ActionSpotify();
            case ALARM_CLOCK_ACTION_TITLE:
                return new ActionAlarmClock();
            case TEXT_MESSAGE_ACTION_TITLE:
                return new ActionSendTextMessage();
            case SOUND_MODE_ACTION_TITLE:
                return new ActionSoundSettings();
            case PHONE_CALL_ACTION_TITLE:
                return new ActionPhoneCall();
            case TIMER_ACTION_TITLE:
                return new ActionSetTimer();
            case BRIGHTNESS_ACTION_TITLE:
                return new ActionBrightness();
            case ORIENTATION_ACTION_TITLE:
                return new ActionChangeOrientation();
            case FLASH_ACTION_TITLE:
                return new ActionOpenFlash();
            case GMAIL_ACTION_TITLE:
                return new ActionGmail();
            case BLUETOOTH_ACTION_TITLE:
                return new ActionBluetooth();
            case READ_AUTO_ACTION_TITLE:
                return new ActionReadAuto();
            case GOOGLE_MAPS_ACTION_TITLE:
                return new ActionGoogleMaps();
            case GPS_ACTION_TITLE:
                return new ActionGps();
        }
        return null;
    }
}
