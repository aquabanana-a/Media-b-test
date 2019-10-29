package com.dobranos.instories.domain.base.interactor;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.os.Environment;
import android.util.Log;

import com.dobranos.instories.domain.base.model.encoder.EncodeBroker;
import com.dobranos.instories.domain.base.model.encoder.video.VideoEncodeListener;
import com.dobranos.instories.domain.base.resolver.encoder.video.old.VideoEncodeResolver;
import com.dobranos.instories.domain.base.resolver.encoder.video.old.VideoStoreResolver;
import com.dobranos.instories.domain.base.resolver.renderer.StreamRenderResolver;

import org.jcodec.common.model.Size;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

public class StreamProcessor implements VideoEncodeListener
{
    private VideoEncodeResolver encoder;
    private StreamRenderResolver renderer;
    private VideoStoreResolver storer;

    private File file;
    private Context context;

    Size frameSize;

    Timer timer;
    int fps;

    public StreamProcessor(File file, Context context, Size frameSize)
    {
        this.file = file;
        this.context = context;
        this.frameSize = frameSize;
        this.encoder = new VideoEncodeResolver(this, frameSize)
         //   .withDesiredEncodeFormat(MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar)
        ;

        this.storer = new VideoStoreResolver(encoder, file);
    }

    public void start()
    {
        timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                Log.d("my", "FPS: " + fps);
                fps = 0;
            }
        }, 0, 1000);

        encoder.setRunning(true);
        encoder.start();
    }

    public void stop()
    {
        encoder.setRunning(false);

        if(timer != null)
        {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    public void onRelease(EncodeBroker broker) { renderer.stop(); }
    public void onPrepare(EncodeBroker broker)
    {
        renderer = new StreamRenderResolver(broker.getBroker(), context);
        renderer.start();
    }

    public void onFrame(ByteBuffer data, MediaCodec.BufferInfo info)
    {
        fps++;
    }

}