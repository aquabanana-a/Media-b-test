package com.dobranos.instories.domain.base.model.encoder.video;

import android.media.MediaCodec;

import com.dobranos.instories.domain.base.model.encoder.EncodeBroker;
import com.dobranos.instories.domain.base.model.listener.IListener;

import java.nio.ByteBuffer;

public interface VideoEncodeListener extends IListener
{
    void onPrepare(EncodeBroker broker);
    void onRelease(EncodeBroker broker);

    void onFrame(ByteBuffer data, MediaCodec.BufferInfo info);
}