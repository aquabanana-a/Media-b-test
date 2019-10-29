package com.dobranos.instories.domain.base.resolver;

import android.graphics.Point;
import android.graphics.PointF;

import androidx.lifecycle.MutableLiveData;
import com.dobranos.instories.domain.base.model.Resolution;

public class ResolutionResolver
{
    private static final float DESIRED_ASPECT_RATIO = 9/16;

    private MutableLiveData<Resolution> resolution = new MutableLiveData<>();

    public MutableLiveData<Resolution> getSurfaceResolution() { return resolution; }

    public void resolve(Point actual)
    {
        int x = 0;
        int y = 0;
        int i = 0;

        do
        {
            x += 9;
            y += 16;
            i ++;
        }
        while(x <= (actual.x - 9) && y <= (actual.y - 16));

        resolution.postValue(new Resolution(
            new PointF(x, y),
            new PointF(.75f * i, .75f * i),
            new PointF(4.75f * i, 9.75f * i),
            new PointF(3.5f * i, 5.5f * i)
        ));
    }
}