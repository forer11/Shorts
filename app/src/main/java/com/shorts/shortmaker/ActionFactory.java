package com.shorts.shortmaker;

import com.shorts.shortmaker.Actions.Action;
import com.shorts.shortmaker.Actions.ActionBrightness;
import com.shorts.shortmaker.Actions.ActionAlarmClock;
import com.shorts.shortmaker.Actions.ActionChangeOrientation;
import com.shorts.shortmaker.Actions.ActionGmail;
import com.shorts.shortmaker.Actions.ActionOpenFlash;
import com.shorts.shortmaker.Actions.ActionPhoneCall;
import com.shorts.shortmaker.Actions.ActionSendTextMessage;
import com.shorts.shortmaker.Actions.ActionSetTimer;
import com.shorts.shortmaker.Actions.ActionSoundSettings;
import com.shorts.shortmaker.Actions.ActionSpotify;
import com.shorts.shortmaker.Actions.ActionWaze;
import com.shorts.shortmaker.DataClasses.ActionData;

import java.util.ArrayList;
import java.util.Arrays;


public class ActionFactory {

    public static final String WAZE_ACTION_TITLE = "Waze";
    public static final String SPOTIFY_ACTION_TITLE = "Spotify";
    public static final String ALARM_CLOCK_ACTION_TITLE = "Alaram clock";
    public static final String TEXT_MESSAGE_ACTION_TITLE = "Text message";
    public static final String SOUND_MODE_ACTION_TITLE = "Sound mode";
    public static final String PHONE_CALL_ACTION_TITLE = "Phone call";
    public static final String TIMER_ACTION_TITLE = "Timer";
    public static final String BRIGHTNESS_ACTION_TITLE = "Brightness";
    public static final String ORIENTATION_ACTION_TITLE = "Orientation";
    public static final String FLASH_ACTION_TITLE = "Flash";
    public static final String GMAIL_ACTION_TITLE = "Gmail";



    public static final ArrayList<ActionData> ACTION_DATA_ARRAY_LIST = new ArrayList<ActionData>(
            Arrays.asList(
                    new ActionData(ActionFactory.WAZE_ACTION_TITLE, R.drawable.waze_icon),
                    new ActionData(ActionFactory.SPOTIFY_ACTION_TITLE, R.drawable.spotify_icon),
                    new ActionData(ActionFactory.ALARM_CLOCK_ACTION_TITLE, R.drawable.alarm_clock_icon),
                    new ActionData(ActionFactory.PHONE_CALL_ACTION_TITLE, R.drawable.phone_icon),
                    new ActionData(ActionFactory.TIMER_ACTION_TITLE, R.drawable.timer_icon),
                    new ActionData(ActionFactory.TEXT_MESSAGE_ACTION_TITLE, R.drawable.text_message),
                    new ActionData(ActionFactory.SOUND_MODE_ACTION_TITLE, R.drawable.sound_mode_icon),
                    new ActionData(ActionFactory.BRIGHTNESS_ACTION_TITLE, R.drawable.wifi_icon),
                    new ActionData(ActionFactory.ORIENTATION_ACTION_TITLE, R.drawable.orientation_icon),
                    new ActionData(ActionFactory.FLASH_ACTION_TITLE, R.drawable.flash_icon),
                    new ActionData(ActionFactory.GMAIL_ACTION_TITLE, R.drawable.gmail_icon)

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
        }
        return null;
    }
}
