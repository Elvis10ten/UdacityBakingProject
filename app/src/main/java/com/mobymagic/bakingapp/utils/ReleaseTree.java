package com.mobymagic.bakingapp.utils;

import android.util.Log;

import timber.log.Timber;

/**
 * A Timber tree for release mode. It ignores Verbose, Debug and Info logs but sends Warning and Error
 * logs to Firebase Crash Reporting service.
 */
public class ReleaseTree extends Timber.Tree {

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            // Ignore any log that have VERBOSE, DEBUG or INFO priority
            return;
        }

        if (t != null) {
            if (priority == Log.ERROR) {
                // Send logs with error priority to firebase/crashlytics crash service
                //FirebaseCrash.report(t);
            } else if (priority == Log.WARN) {
                // Send logs with warning priority to firebase/crashlytics crash service
                //FirebaseCrash.report(t);
            }
        }
    }

}