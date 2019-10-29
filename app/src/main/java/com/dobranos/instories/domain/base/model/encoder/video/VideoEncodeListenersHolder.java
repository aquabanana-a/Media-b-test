package com.dobranos.instories.domain.base.model.encoder.video;

import android.media.MediaCodec;

import com.dobranos.instories.domain.base.model.encoder.EncodeBroker;
import com.dobranos.instories.domain.base.model.listener.IListenerMgr;
import com.dobranos.instories.domain.base.model.listener.ListenerMgr;

import java.nio.ByteBuffer;

public class VideoEncodeListenersHolder<T extends IListenerMgr> extends ListenerMgr<T, VideoEncodeListener>
{
    public void callOnPrepare(final EncodeBroker broker)
    {
        for (final VideoEncodeListener listener : getSynchronizedListeners())
            new Thread(() ->
            {
                try { listener.onPrepare(broker); }
                catch (Exception e) { }
            }).start();
    }

    public void callOnRelease(final EncodeBroker broker)
    {
        for (final VideoEncodeListener listener : getSynchronizedListeners())
            new Thread(() ->
            {
                try { listener.onRelease(broker); }
                catch (Exception e) { }
            }).start();
    }

    public void callOnFrame(final ByteBuffer data, final MediaCodec.BufferInfo info)
    {
        for (final VideoEncodeListener listener : getSynchronizedListeners())
            new Thread(() ->
            {
                try { listener.onFrame(data, info); }
                catch (Exception e) { }
            }).start();
    }
}