package com.example.robert.traceit.Utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by Robert on 3/7/2017.
 */

public class Utils {

    public static float dpConverter(float dpSize, DisplayMetrics dm)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, dm);
    }

}
