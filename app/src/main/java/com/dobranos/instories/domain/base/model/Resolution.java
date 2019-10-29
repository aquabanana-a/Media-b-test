package com.dobranos.instories.domain.base.model;

import android.graphics.Point;
import android.graphics.PointF;

public class Resolution
{
    private PointF surfaceSize;
    private PointF gapSize;
    private PointF bigHolderSize;
    private PointF smallHolderSize;

    public Resolution(PointF ss, PointF gs, PointF bhs, PointF sms)
    {
        surfaceSize = ss;
        gapSize = gs;
        bigHolderSize = bhs;
        smallHolderSize = sms;
    }

    public PointF getSurfaceSizePx()     { return surfaceSize; }
    public PointF getGapSizePx()         { return gapSize; }
    public PointF getBigHolderSizePx()   { return bigHolderSize; }
    public PointF getSmallHolderSizePx() { return smallHolderSize; }
}