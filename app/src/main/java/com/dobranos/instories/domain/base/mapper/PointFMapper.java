package com.dobranos.instories.domain.base.mapper;

import android.graphics.PointF;

import org.jcodec.common.model.Size;

public class PointFMapper
{
    public static Size toSize(PointF value) { return new Size((int)value.x, (int)value.y); }
}
