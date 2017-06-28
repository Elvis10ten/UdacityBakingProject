package com.mobymagic.bakingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mobymagic.bakingapp.BakingApp;

public class WirelessUtils {

    /**
     * Checks if the device is currently connected to the internet. Please note this method is not
     * a guarantee that network calls will succeed, it only tells if the device data connection is on
     * or the device is connected to a wifi. The only way to truly confirm is to make a network call
     * @return true if the device is connected to the internet, false otherwise
     */
    public static boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) BakingApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
