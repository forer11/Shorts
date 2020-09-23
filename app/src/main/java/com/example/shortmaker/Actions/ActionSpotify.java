package com.example.shortmaker.Actions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.example.shortmaker.DataClasses.Action;

public class ActionSpotify implements Action {

    public static final String SPOTIFY_PACKAGE_NAME = "com.spotify.music";
    private Context context;

    public ActionSpotify(Context context) {
        this.context = context;
    }

    @Override
    public void activate() {
        boolean isSpotifyInstalled = checkIfSpotifyInstalled();
        if (isSpotifyInstalled) {
            launchSpotify();
        } else {
            installSpotify();
        }
    }

    @Override
    public void setData(String stringData, Integer intData) {

    }

    private void installSpotify() {
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

    private void launchSpotify() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //TODO - we can generally open spotify with "spotify:open" and not a specific album
        // we can delete the ":play: from the uri in order for the song not to be played automatically
        intent.setData(Uri.parse("spotify:album:0sNOF9WDwhWunNAHPD3Baj:play"));
        intent.putExtra(Intent.EXTRA_REFERRER,
                Uri.parse("android-app://" + context.getPackageName()));
        context.startActivity(intent);
    }

    private boolean checkIfSpotifyInstalled() {
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
