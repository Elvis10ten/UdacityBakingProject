package com.mobymagic.bakingapp.utils;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;

public class WindowUtils {

    private static final String LOG_TAG = "WindowUtils";

    /**
     * Makes the status bar translucent for the specified activity. This only works for kitkat and above.
     * Calling this method on a device with version lower than kitkat has no effect
     * @param activity The activity to make translucent
     */
    public static void makeStatusBarTranslucent(@NonNull Activity activity) {

        if (VersionUtils.hasKitKat()) {
            Window w = activity.getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * Sets the status bar for the Activity to the specified color. This method only changes the status
     * bar color on Lollipop and above devices
     * @param activity An activity
     * @param color The color to set the status bar to
     */
    public static void setStatusBar(@NonNull Activity activity, @ColorInt int color) {
        if(VersionUtils.hasLollipop()) {
            Window window = activity.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(color);
        }
    }

}
