package com.dobranos.instories.domain.base.resolver.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.view.Surface;

import com.dobranos.instories.domain.base.model.renderer.Renderer;
import com.dobranos.instories.domain.base.model.renderer.RendererListener;

public class StreamRenderResolver extends Renderer implements RendererListener
{
    private TextPaint paint;
    private long timeStart;
    private Context context;
    private Renderer renderer;

    public StreamRenderResolver(Object broker, Context context)
    {
        super(null);
        this.context = context;

        if (broker instanceof Bitmap)
            this.renderer = new BitmapRenderResolver((Bitmap)broker, this);
        else if (broker instanceof Surface)
            this.renderer = new SurfaceRenderResolver((Surface)broker, this);
        else
            throw new IllegalArgumentException();
    }

    public void start()
    {
        renderer.start();
    }

    public void onStart()
    {
        timeStart = System.currentTimeMillis();
    }

    public void stop()
    {
        renderer.stop();
    }

    public void onStop()
    {

    }

    String formatTime()
    {
        int now = (int) (System.currentTimeMillis() - timeStart);
        int minutes = now / 1000 / 60;
        int seconds = now / 1000 % 60;
        int millis = now % 1000;
        return String.format("%02d:%02d:%03d", minutes, seconds, millis);
    }

    public void onDraw(Canvas canvas)
    {
        canvas.drawColor(Color.BLACK);

        if (paint == null)
        {
            paint = new TextPaint();
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setTextSize(60f * context.getResources().getConfiguration().fontScale);
            paint.setTextAlign(Paint.Align.CENTER);
        }

        canvas.drawText(formatTime(),
            1280 / 2,
            720 / 2,
            paint);
    }
}
