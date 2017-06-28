package com.mobymagic.bakingapp;

import android.app.Application;
import android.os.StrictMode;

import com.mobymagic.bakingapp.utils.ReleaseTree;

import java.io.File;

import timber.log.Timber;

public class BakingApp extends Application {

    private static BakingApp sCurrentApplication = null;

    // region Lifecycle Methods
    @Override
    public void onCreate() {
        super.onCreate();
        sCurrentApplication = this;

        initTimber();
        initStrictModeOnDebug();
    }
    // endregion

    // region Initialization Methods
    private void initStrictModeOnDebug() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }
    }
    // endregion

    // region Helper Methods
    public static BakingApp getInstance() {
        return sCurrentApplication;
    }

    public static File getCacheDirectory() {
        return sCurrentApplication.getCacheDir();
    }
    // endregion

}
