package com.example.shortmaker;

import com.example.shortmaker.Actions.Action;
import com.example.shortmaker.Actions.ActionActivateWifi;
import com.example.shortmaker.Actions.ActionAlarmClock;
import com.example.shortmaker.Actions.ActionPhoneCall;
import com.example.shortmaker.Actions.ActionSendTextMessage;
import com.example.shortmaker.Actions.ActionSetTimer;
import com.example.shortmaker.Actions.ActionSoundSettings;
import com.example.shortmaker.Actions.ActionSpotify;
import com.example.shortmaker.Actions.ActionWaze;
import com.example.shortmaker.DataClasses.ActionData;

import java.util.ArrayList;
import java.util.Arrays;


public class ActionFactory {

    public static final String WAZE_ACTION_TITLE = "waze";
    public static final String SPOTIFY_ACTION_TITLE = "spotify";
    public static final String ALARM_CLOCK_ACTION_TITLE = "alaram clock";
    public static final String TEXT_MESSAGE_ACTION_TITLE = "text message";
    public static final String SOUND_MODE_ACTION_TITLE = "sound mode";
    public static final String PHONE_CALL_ACTION_TITLE = "phone call";
    public static final String TIMER_ACTION_TITLE = "timer";
    public static final String WIFI_ACTION_TITLE = "wifi";

    public static final ArrayList<ActionData> ACTION_DATA_ARRAY_LIST = new ArrayList<ActionData>(
            Arrays.asList(
                    new ActionData(ActionFactory.WAZE_ACTION_TITLE, R.drawable.waze_icon),
                    new ActionData(ActionFactory.SPOTIFY_ACTION_TITLE, R.drawable.spotify_icon),
                    new ActionData(ActionFactory.ALARM_CLOCK_ACTION_TITLE, R.drawable.alarm_clock_icon),
                    new ActionData(ActionFactory.PHONE_CALL_ACTION_TITLE, R.drawable.phone_icon),
                    new ActionData(ActionFactory.TIMER_ACTION_TITLE, R.drawable.timer_icon),
                    new ActionData(ActionFactory.TEXT_MESSAGE_ACTION_TITLE, R.drawable.text_message),
                    new ActionData(ActionFactory.SOUND_MODE_ACTION_TITLE, R.drawable.sound_mode_icon),
                    new ActionData(ActionFactory.WIFI_ACTION_TITLE, R.drawable.wifi_icon)
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
            case WIFI_ACTION_TITLE:
                return new ActionActivateWifi();
        }
        return null;
    }
}
