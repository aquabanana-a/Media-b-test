package com.dobranos.instories.domain.base.resolver.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.dobranos.instories.domain.base.model.renderer.Renderer;
import com.dobranos.instories.domain.base.model.renderer.RendererListener;

public class BitmapRenderResolver extends Renderer
{
    private Bitmap bitmap;
    private Canvas canvas;
    private Renderer renderer;
    private RendererListener listener;

    public BitmapRenderResolver(Bitmap bitmap, RendererListener listener)
    {
        super(listener);
        this.bitmap = bitmap;
        this.canvas = new Canvas(bitmap);
        this.listener = listener;
    }

    protected void onDraw(Canvas canvas)
    {
        listener.onDraw(canvas);
    }

    public void start()
    {
        if (renderer != null)
            return;

        listener.onStart();

        renderer = new Renderer();
        renderer.setRunning(true);
        renderer.start();
    }

    public void stop()
    {
        if (renderer == null)
            return;

        listener.onStop();
        renderer.setRunning(false);

        try
        {
            renderer.join();
        }
        catch (InterruptedException ignore) { }
        renderer = null;
    }

    class Renderer extends Thread
    {
        private volatile boolean isRunning;

        public void setRunning(boolean running)
        {
            isRunning = running;
        }

        @Override
        public void run()
        {
            while (isRunning)
            {
                synchronized (bitmap)
                {
                    onDraw(canvas);
                }
                try
                {
                    Thread.sleep(1);
                }
                catch (Exception e) { }
            }
        }
    }
}