package com.dobranos.instories.presentation.base.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.Display;

public class DeviceUtil
{
    public static Point getScreenSizeInPx(Activity act)
    {
        Display display = act.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static PointF getScreenSizeInDp(Context cx)
    {
        DisplayMetrics displayMetrics = cx.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return new PointF(dpWidth, dpHeight);
    }
}
