package com.dobranos.instories.domain.base.model.renderer;

import android.graphics.Canvas;

public interface RendererListener
{
    void onStart();
    void onStop();

    void onDraw(Canvas canvas);
}
