package com.dobranos.instories.domain.base.model.renderer;

import android.graphics.Canvas;

public abstract class Renderer
{
    public Renderer(RendererListener listener) {}

    public abstract void start();
    public abstract void stop();

    protected abstract void onDraw(Canvas canvas);
}