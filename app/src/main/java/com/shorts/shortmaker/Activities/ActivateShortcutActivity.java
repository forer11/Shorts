package com.shorts.shortmaker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.shorts.shortmaker.ActionFactory;
import com.shorts.shortmaker.Actions.Action;
import com.shorts.shortmaker.AppData;
import com.shorts.shortmaker.DataClasses.ActionData;
import com.shorts.shortmaker.DataClasses.Shortcut;
import com.shorts.shortmaker.FireBaseHandlers.FireStoreHandler;
import com.shorts.shortmaker.R;

public class ActivateShortcutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activiate_shortcut);
        AppData appData = (AppData) getApplicationContext();
        if (getIntent().getExtras() != null) {
            String id = getIntent().getStringExtra("shortcutId");
            appData.fireStoreHandler.getShortcut(id, new FireStoreHandler.SingleShortcutCallback() {
                @Override
                public void onAddedShortcut(String id, Shortcut shortcut, Boolean success) {
                    if (success) { //TODO check failure
                        for (ActionData actionData : shortcut.getActionDataList()) {
                            if (actionData.getIsActivated()) {
                                Action action = ActionFactory.getAction(actionData.getTitle());
                                if (action != null) {
                                    action.setData(actionData.getData());
                                    action.activate(getApplication(),
                                            ActivateShortcutActivity.this);
                                }
                            }
                        }
                        finish();
                    }
                }
            });
        }
    }
}