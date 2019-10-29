package com.dobranos.instories.domain.base.resolver.renderer;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.Surface;

import com.dobranos.instories.domain.base.model.renderer.Renderer;
import com.dobranos.instories.domain.base.model.renderer.RendererListener;

public class SurfaceRenderResolver extends Renderer
{
    private Surface surface;
    private Renderer renderer;
    private RendererListener listener;

    public SurfaceRenderResolver(Surface surface, RendererListener listener)
    {
        super(listener);
        this.surface = surface;
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
                Canvas canvas = surface.lockCanvas(null);
                try
                {
                    onDraw(canvas);
                }
                finally
                {
                    surface.unlockCanvasAndPost(canvas);
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