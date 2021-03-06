package com.stardust.scriptdroid.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.stardust.pio.PFile;
import com.stardust.scriptdroid.App;
import com.stardust.scriptdroid.R;
import com.stardust.scriptdroid.tool.UpdateChecker;
import com.stardust.scriptdroid.tool.VersionInfo;
import com.stardust.scriptdroid.ui.update.UpdateCheckDialog;
import com.stardust.theme.ThemeColorManager;
import com.stardust.util.BackPressedHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by Stardust on 2017/1/23.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected static final int PERMISSION_REQUEST_CODE = 11186;
    private BackPressedHandler.Observer mBackPressObserver = new BackPressedHandler.Observer();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ((getWindow().getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) == 0) {
            ThemeColorManager.addActivityStatusBar(this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    protected void onPause() {
        super.onPause();
        VersionInfo.getInstance().setOnReceiveUpdateResultCallback(null);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T $(int resId) {
        return (T) findViewById(resId);
    }

    protected void checkPermission(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] requestPermissions = getRequestPermissions(permissions);
            if (requestPermissions.length > 0)
                requestPermissions(requestPermissions, PERMISSION_REQUEST_CODE);
        } else {
            int[] grantResults = new int[permissions.length];
            Arrays.fill(grantResults, PERMISSION_GRANTED);
            onRequestPermissionsResult(PERMISSION_REQUEST_CODE, permissions, grantResults);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private String[] getRequestPermissions(String[] permissions) {
        List<String> list = new ArrayList<>();
        for (String permission : permissions) {
            if (checkSelfPermission(permission) == PERMISSION_DENIED) {
                list.add(permission);
            }
        }
        return list.toArray(new String[list.size()]);
    }

    public void setToolbarAsBack(String title) {
        setToolbarAsBack(this, R.id.toolbar, title);
    }

    public void registerBackPressedHandler(BackPressedHandler handler) {
        mBackPressObserver.registerHandler(handler);
    }

    @Override
    public void onBackPressed() {
        if (!mBackPressObserver.onBackPressed(this)) {
            super.onBackPressed();
        }
    }

    public static void setToolbarAsBack(final AppCompatActivity activity, int id, String title) {
        Toolbar toolbar = (Toolbar) activity.findViewById(id);
        toolbar.setTitle(title);
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
