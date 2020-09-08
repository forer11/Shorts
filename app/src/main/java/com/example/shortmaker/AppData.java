package com.example.shortmaker;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.shortmaker.FireBaseHandlers.FireBaseAuthHandler;
import com.example.shortmaker.FireBaseHandlers.FireStoreHandler;
import com.maltaisn.icondialog.pack.IconPack;
import com.maltaisn.icondialog.pack.IconPackLoader;
import com.maltaisn.iconpack.fa.IconPackFa;
import com.maltaisn.iconpack.mdi.IconPackMdi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Locale;

public class AppData extends Application {
    FireStoreHandler fireStoreHandler;
    FireBaseAuthHandler fireBaseAuthHandler;
    @Nullable
    private IconPack iconPack;
    @Override
    public void onCreate() {
        super.onCreate();
        fireBaseAuthHandler = new FireBaseAuthHandler(getApplicationContext());
        fireStoreHandler = new FireStoreHandler(getApplicationContext());
        printHashKey();
        loadIconPack();

    }


    @Nullable
    public IconPack getIconPack() {
        return iconPack != null ? iconPack : loadIconPack();
    }

    private IconPack loadIconPack() {
        // Create an icon pack loader with application context.
        IconPackLoader loader = new IconPackLoader(this);
        // Create an icon pack and load all drawables.
        IconPack parentIconPack = IconPackFa.createFontAwesomeIconPack(loader);
        parentIconPack.loadDrawables(loader.getDrawableLoader());
        iconPack = loader.load(R.xml.icons, R.xml.tags, Collections.singletonList(Locale.ENGLISH), parentIconPack);
        return iconPack;
    }

    public void printHashKey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.shortmaker",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (
                PackageManager.NameNotFoundException e) {
            Log.d("ERROR", e.toString());

        } catch (
                NoSuchAlgorithmException e) {
            Log.d("ERROR", e.toString());
        }
    }
}
