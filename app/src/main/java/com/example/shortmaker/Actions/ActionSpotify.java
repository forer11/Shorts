package com.example.shortmaker.Actions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionDialogs.SpotifyDialog;

import java.util.List;

public class ActionSpotify implements Action {

    private static final String SPOTIFY_PACKAGE_NAME = "com.spotify.music";
    private SpotifyDialog dialog;

    public ActionSpotify() {
        this.dialog = new SpotifyDialog();
    }

    @Override
    public void activate(Context context) {
        Log.v("YAY", "Spotify activated");
        Toast.makeText(context, "Spotify activated", Toast.LENGTH_SHORT).show();
//        boolean isSpotifyInstalled = checkIfSpotifyInstalled(context);
//        if (isSpotifyInstalled) {
//            launchSpotify(context);
//        } else {
//            installSpotify(context);
//        }
    }

    @Override
    public ActionDialog getDialog() {
        return dialog;
    }

    @Override
    public void setData(List<String> data) {

    }

    private void installSpotify(Context context) {
        final String referrer = "adjust_campaign=PACKAGE_NAME&adjust_tracker=ndjczk&utm_source=adjust_preinstall";
        try {
            Uri uri = Uri.parse("market://details")
                    .buildUpon()
                    .appendQueryParameter("id", SPOTIFY_PACKAGE_NAME)
                    .appendQueryParameter("referrer", referrer)
                    .build();
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (ActivityNotFoundException ignored) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details")
                    .buildUpon()
                    .appendQueryParameter("id", SPOTIFY_PACKAGE_NAME)
                    .appendQueryParameter("referrer", referrer)
                    .build();
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

    private void launchSpotify(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //TODO - we can generally open spotify with "spotify:open" and not a specific album
        // we can delete the ":play: from the uri in order for the song not to be played automatically
        intent.setData(Uri.parse("spotify:album:0sNOF9WDwhWunNAHPD3Baj:play"));
        intent.putExtra(Intent.EXTRA_REFERRER,
                Uri.parse("android-app://" + context.getPackageName()));
        context.startActivity(intent);

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
