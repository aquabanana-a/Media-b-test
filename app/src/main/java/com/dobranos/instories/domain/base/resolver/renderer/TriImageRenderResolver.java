package com.dobranos.instories.domain.base.resolver.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaCodec;
import android.os.Environment;
import android.text.TextPaint;
import android.util.Log;

import com.dobranos.instories.domain.base.model.encoder.EncodeBroker;
import com.dobranos.instories.domain.base.model.encoder.video.VideoEncodeListener;
import com.dobranos.instories.domain.base.resolver.ResolutionResolver;
import com.dobranos.instories.domain.base.resolver.encoder.video.old.VideoEncodeResolver;
import com.dobranos.instories.domain.base.resolver.encoder.video.old.VideoStoreResolver;
import com.dobranos.instories.view.base.ui.App;

import org.jcodec.common.model.Size;

import java.io.File;
import java.nio.ByteBuffer;

import javax.inject.Inject;

public class TriImageRenderResolver
{
    private TextPaint paint;
    private long timeStart;
    private Context context;

    @Inject protected ResolutionResolver resolution;
    private VideoEncodeResolver encoder;
    private VideoStoreResolver storer;

    private Renderer renderer;
    private Object broker;

    public TriImageRenderResolver(Context context)
    {
        App.get(null).getAppComponent().inject(this);

        this.context = context;

        try
        {
            this.encoder = new VideoEncodeResolver(new VideoEncodeListener()
            {
                @Override
                public void onPrepare(EncodeBroker broker)
                {
                    TriImageRenderResolver.this.broker = broker.getBroker();

                    renderer = new Renderer();
                    renderer.setRunning(true);
                    renderer.start();
                }

                @Override
                public void onRelease(EncodeBroker broker)
                {

                }

                @Override
                public void onFrame(ByteBuffer data, MediaCodec.BufferInfo info)
                {

                }
            }, new Size(360, 640)/*PointFMapper.toSize(resolution.getSurfaceResolution().getValue().getSurfaceSizePx())*/)
               //.withDesiredEncodeFormat(MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar)
            ;

            this.storer = new VideoStoreResolver(encoder, new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "XYZ"), "someone");

//            Thread t = new Thread(() ->
//            {
//                try
//                {
//                    broker = this.encoder.prepare();
//                }
//                catch (Exception e) { e.printStackTrace(); }
//            });
//
//            t.start();
//            t.join();



            Log.d("my", "TriImageRenderResolver frameSize:" + encoder.getFrameSize().getWidth() + ";" + encoder.getFrameSize().getHeight());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void start()
    {
//        renderer = new Renderer();
//        renderer.setRunning(true);
//        renderer.start();

        encoder.setRunning(true);
        encoder.start();

//        if (renderer != null)
//            return;

//        encoder.setRunning(true);
//        encoder.start();


    }

    public void stop()
    {
        stopImpl();
    }

    String formatTime()
    {
        int now = (int) (System.currentTimeMillis() - timeStart);
        int minutes = now / 1000 / 60;
        int seconds = now / 1000 % 60;
        int millis = now % 1000;
        return String.format("%02d:%02d:%03d", minutes, seconds, millis);
    }

    Paint p;

    private void preDraw()
    {
        p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);

        Log.d("my", "preDraw");
    }

    public boolean onDraw(Canvas canvas, long syncMs, long frame)
    {
        if(syncMs == 0)
            preDraw();

        canvas.drawColor(Color.BLACK);


//        int xpos = (int) (50 + 0.2 * frame);
//        canvas.drawCircle(xpos, canvas.getHeight()/2, 25, p);


        Log.d("my", "onDraw");


        if(frame >= 100)
        {
            stopImpl();
            return true;
        }
        return false;
    }

    private void stopImpl()
    {
        Log.d("my", "stopImpl");

        //encoder.signalEOS();
        renderer.setRunning(false);
//        try
//        {
//            renderer.join();
//        }
//        catch (InterruptedException ignore) { }
//        renderer = null;

        //encoder.setRunning(false);
        //encoder.release();

        //Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
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
            Canvas canvas;
            long lastFrameMs = 0;
            long nowMs = 0;
            long frame = 1;
            boolean lastFrame = false;

//            try
//            {
//                broker = encoder.prepare();
//            }
//            catch (Exception e) { e.printStackTrace(); }

//            if(broker instanceof Bitmap)
//            {
//                canvas = new Canvas((Bitmap) broker);
//                while (isRunning)
//                {
//                    synchronized (broker)
//                    {
//                        nowMs = System.currentTimeMillis();
//                        //encoder.encodeSync(frame, true);
//                        lastFrame = onDraw(canvas, lastFrameMs > 0 ? (nowMs - lastFrameMs) : 0, frame);
//                        lastFrameMs = nowMs;
//                    }
//                    try
//                    {
//                        Thread.sleep(10);
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                    encoder.encodeSync(frame, lastFrame);
//                    frame++;
//                }
//            }
//            else if (broker instanceof Surface)
//            {
//                Surface surface = (Surface)broker;
//                while (!lastFrame && isRunning)
//                {
//                    try
//                    {
//                        //canvas = surface.lockCanvas(null);
//                        try
//                        {
//                            nowMs = System.currentTimeMillis();
//                            //lastFrame = onDraw(canvas, lastFrameMs > 0 ? (nowMs - lastFrameMs) : 0, frame);
//                            lastFrameMs = nowMs;
//                        }
//                        catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                        finally
//                        {
//                            //surface.unlockCanvasAndPost(canvas);
//                        }
//
//
////                        encoder.encodeSync(frame, lastFrame);
//                        frame++;
//
////                        encoder.setRunning(false);
//                    }
//                    catch (Exception e) { e.printStackTrace(); }
//
//                }
//            }
        }
    }
}
