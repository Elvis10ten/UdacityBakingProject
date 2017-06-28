package com.mobymagic.bakingapp.utils;

public class DisplayUtils {

    public static int getGridSpanCount(int measuredWidth, int gridWidth) {
        int newSpanCount = (int) Math.floor(measuredWidth / gridWidth);

        if (newSpanCount == 0) {
            newSpanCount = 2;
        }

        return newSpanCount;
    }

}
