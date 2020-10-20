package com.shorts.shortmaker.Actions;

import android.app.Activity;
import android.app.Application;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionDialogs.SpotifyDialog;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.shorts.shortmaker.AppData.inBackground;

public class ActionSpotify implements Action {

    private static final String SPOTIFY_PACKAGE_NAME = "com.spotify.music";
    private SpotifyDialog dialog;
    private String artist;

    public ActionSpotify() {
        this.dialog = new SpotifyDialog();
    }

    @Override
    public void activate(Application application, Context context) {
        Log.v("YAY", "Spotify activated");
        boolean isSpotifyInstalled = checkIfSpotifyInstalled(context);
        if (isSpotifyInstalled) {
//            launchSpotify(context,context);
            playSearchArtist(context, artist);
        } else {
            installSpotify(context, inBackground);
        }
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {
        artist = data.get(0);
    }

    private void installSpotify(Context activity, boolean isNewTask) {
        final String referrer = "adjust_campaign=PACKAGE_NAME&adjust_tracker=ndjczk&utm_source=adjust_preinstall";
        try {
            Uri uri = Uri.parse("market://details")
                    .buildUpon()
                    .appendQueryParameter("id", SPOTIFY_PACKAGE_NAME)
                    .appendQueryParameter("referrer", referrer)
                    .build();
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (isNewTask) {
                intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
            }
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException ignored) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details")
                    .buildUpon()
                    .appendQueryParameter("id", SPOTIFY_PACKAGE_NAME)
                    .appendQueryParameter("referrer", referrer)
                    .build();
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (isNewTask) {
                intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
            }
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }

    private void launchSpotify(Context context, Context activity, boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (isNewTask) {
            intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
        }
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        //TODO - we can generally open spotify with "spotify:open" and not a specific album
        // we can delete the ":play: from the uri in order for the song not to be played automatically
        intent.setData(Uri.parse("spotify:album:" + artist + ":play"));
        intent.putExtra(Intent.EXTRA_REFERRER,
                Uri.parse("android-app://" + context.getPackageName()));
        activity.startActivity(intent);
    }

    public void playSearchArtist(Context activity, String artist) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
        intent.setComponent(new ComponentName("com.spotify.music", "com.spotify.music.MainActivity"));
        intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS, MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE);
        intent.putExtra(MediaStore.EXTRA_MEDIA_ARTIST, artist);
        intent.putExtra(SearchManager.QUERY, artist);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }

    }


    private boolean checkIfSpotifyInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean isSpotifyInstalled;
        try {
            pm.getPackageInfo(SPOTIFY_PACKAGE_NAME, 0);
            isSpotifyInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isSpotifyInstalled = false;
        }
        return isSpotifyInstalled;
    }

}
