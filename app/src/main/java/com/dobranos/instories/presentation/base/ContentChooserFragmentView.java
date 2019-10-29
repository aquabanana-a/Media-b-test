package com.dobranos.instories.presentation.base;

import android.app.Activity;
import android.graphics.Point;
import com.dobranos.instories.domain.base.model.Resolution;

public interface ContentChooserFragmentView
{
    void setChooserSurfaceResolution(Resolution value);

    Activity getActivity();
}
