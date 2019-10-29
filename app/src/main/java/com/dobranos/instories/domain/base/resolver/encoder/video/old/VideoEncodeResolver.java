package com.dobranos.instories.domain.base.resolver.encoder.video.old;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;
import android.view.Surface;

import com.dobranos.instories.domain.base.mapper.YuvMapper;
import com.dobranos.instories.domain.base.model.encoder.EncodeBroker;
import com.dobranos.instories.domain.base.model.encoder.video.VideoEncodeListener;
import com.dobranos.instories.domain.base.model.encoder.video.VideoEncodeListenersHolder;

import org.jcodec.common.model.Size;

import java.io.IOException;
import java.nio.ByteBuffer;

import static android.media.MediaFormat.KEY_BIT_RATE;
import static android.media.MediaFormat.KEY_COLOR_FORMAT;
import static android.media.MediaFormat.KEY_FRAME_RATE;
import static android.media.MediaFormat.KEY_HEIGHT;
import static android.media.MediaFormat.KEY_I_FRAME_INTERVAL;
import static android.media.MediaFormat.KEY_WIDTH;

public class VideoEncodeResolver extends Thread
{
    MediaCodec.BufferInfo mBufferInfo; // хранит информацию о текущем буфере
    MediaCodec codec; // кодер
    MediaFormat format;

    private int[] argb;
    Object mBrokerObj;

    volatile boolean mRunning;
    final long mTimeoutUsec; // блокировка в ожидании доступного буфера

    VideoEncodeListenersHolder eventsHolder;

    public MediaFormat getFormat() { return codec.getOutputFormat(); }
    public Size getFrameSize() { MediaFormat mf = getFormat(); return new Size(mf.getInteger(KEY_WIDTH), mf.getInteger(KEY_HEIGHT)); }

    Size frameSize;

    public VideoEncodeResolver(VideoEncodeListener listener, Size frameSize)
    {
        this.frameSize = frameSize;
        this.eventsHolder = new VideoEncodeListenersHolder();

        addListener(listener);

        mBufferInfo = new MediaCodec.BufferInfo();
        mTimeoutUsec = 10000l;
    }

    public VideoEncodeResolver addListener(VideoEncodeListener listener) { eventsHolder.addListener(listener); return this; }
    public VideoEncodeResolver removeListener(VideoEncodeListener listener) { eventsHolder.removeListener(listener); return this; }

    private boolean IsDesiredEncFmtYuv420sp() { return desiredEncodeFormat == MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar; }
    private int desiredEncodeFormat = -1;
    public VideoEncodeResolver withDesiredEncodeFormat(int format /* MediaCodecInfo.CodecCapabilities */)
    {
        desiredEncodeFormat = format;
        return this;
    }

    public void setRunning(boolean running)
    {
        mRunning = running;
    }

    @Override
    public void run()
    {
        try
        {
            prepare();
            while (mRunning)
            {
                encode();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            release();
        }
    }

    public Object prepare() throws IOException
    {
        int width = frameSize.getWidth();
        int height = frameSize.getHeight();
        int videoBitrate = 400000; // битрейт видео в bps (бит в секунду)
        int videoFramePerSecond = 30;
        int iframeInterval = 2; // I-Frame интервал в секундах

        int colorFormat = (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2  || IsDesiredEncFmtYuv420sp()) ? MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar
                                                                                                                     : MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface;

        format = MediaFormat.createVideoFormat("video/avc", width, height);
        format.setInteger(KEY_COLOR_FORMAT, colorFormat);
        format.setInteger(KEY_BIT_RATE, videoBitrate);
        format.setInteger(KEY_FRAME_RATE, videoFramePerSecond);
        format.setInteger(KEY_I_FRAME_INTERVAL, iframeInterval);

        codec = MediaCodec.createEncoderByType("video/avc");
        codec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);

        mBrokerObj = (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 || IsDesiredEncFmtYuv420sp()) ? Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                                                                                                               : createInputSurface(codec);

        codec.start();
        eventsHolder.callOnPrepare(new EncodeBroker(mBrokerObj));

        return mBrokerObj;
    }

    private void encodeProceed(ByteBuffer data, MediaCodec.BufferInfo info)
    {
        eventsHolder.callOnFrame(data, info);
    }

    private void encode()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 || IsDesiredEncFmtYuv420sp())
            encode_pre_JELLY_BEAN_MR2();
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            encode_pre_LOLLIPOP();
        else
            encode_pre_PIE();
    }

    @SuppressWarnings("deprecation")
    @TargetApi(16)
    private void encode_pre_JELLY_BEAN_MR2()
    {
        Bitmap bmp = (Bitmap) mBrokerObj;
        if(argb == null)
            argb = new int[bmp.getWidth() * bmp.getHeight()];

        ByteBuffer bb;
        synchronized (bmp)
        {
            bmp.getPixels(argb, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
//            bb = BitmapOperationsMapper.jniStoreBitmapData(bmp);
        }

        ByteBuffer[] inputBuffers = codec.getInputBuffers();
        ByteBuffer[] outputBuffers = codec.getOutputBuffers();
        int inputBufferIndex = codec.dequeueInputBuffer(-1);
        if (inputBufferIndex >= 0)
        {
            ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];

            byte[] inputMapped = YuvMapper.ARGBtoYUV420SemiPlanar/*Native*/(argb/*convert(bb.array())*/, bmp.getWidth(), bmp.getHeight());

            inputBuffer.clear();
            inputBuffer.put(inputMapped);
            codec.queueInputBuffer(inputBufferIndex, 0, inputMapped.length, 0, 0);
        }

        int status = codec.dequeueOutputBuffer(mBufferInfo, mTimeoutUsec);
        while (status >= 0)
        {
            ByteBuffer outputBuffer = outputBuffers[status];
            byte[] ouput = new byte[mBufferInfo.size];
            outputBuffer.get(ouput);
            ByteBuffer data = ByteBuffer.wrap(ouput);

            encodeProceed(data, mBufferInfo);

            codec.releaseOutputBuffer(status, false);
            status = codec.dequeueOutputBuffer(mBufferInfo, mTimeoutUsec);
        }
    }

    @SuppressWarnings("deprecation")
    @TargetApi(20)
    private Surface createInputSurface(MediaCodec encoder)
    {
        return encoder.createInputSurface();
    }

    @SuppressWarnings("deprecation")
    @TargetApi(20)
    private void encode_pre_LOLLIPOP()
    {
        if(!mRunning)
            codec.signalEndOfInputStream();

        ByteBuffer[] outputBuffers = codec.getOutputBuffers();
        for (; ; )
        {
            int status = codec.dequeueOutputBuffer(mBufferInfo, mTimeoutUsec);
            if (status == MediaCodec.INFO_TRY_AGAIN_LATER)
            {
                if (!mRunning) break;
            }
            else if (status == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED)
            {
                outputBuffers = codec.getOutputBuffers();
            }
            else if (status >= 0)
            {
                ByteBuffer data = outputBuffers[status];
                data.position(mBufferInfo.offset);
                data.limit(mBufferInfo.offset + mBufferInfo.size);
                final int endOfStream = mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM;

                if (endOfStream == 0)
                {
                    byte[] ouput = new byte[mBufferInfo.size];
                    data.get(ouput);

                    encodeProceed(ByteBuffer.wrap(ouput), mBufferInfo);
                }

                codec.releaseOutputBuffer(status, false);
                if (endOfStream == MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                    break;
            }
        }
    }

    @TargetApi(28)
    private void encode_pre_PIE()
    {
        if (!mRunning)
            codec.signalEndOfInputStream();

        for (; ; )
        {
            int status = codec.dequeueOutputBuffer(mBufferInfo, 0);
            if (status == MediaCodec.INFO_TRY_AGAIN_LATER)
            {
                if (!mRunning) break;
            }
            else if (status >= 0)
            {
                ByteBuffer data = codec.getOutputBuffer(status);
                if (data != null)
                {
                    final int endOfStream = mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM;

                    if (endOfStream == 0)
                    {
                        data.position(mBufferInfo.offset);
                        data.limit(mBufferInfo.offset + mBufferInfo.size);

                        byte[] ouput = new byte[mBufferInfo.size];
                        data.get(ouput);

                        encodeProceed(ByteBuffer.wrap(ouput), mBufferInfo);
                    }

                    codec.releaseOutputBuffer(status, false);
                    if (endOfStream == MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                        break;
                }
            }
        }
    }

    private void release()
    {
        eventsHolder.callOnRelease(new EncodeBroker(mBrokerObj));

        codec.stop();
        codec.release();

        if (mBrokerObj instanceof Surface)
            ((Surface)mBrokerObj).release();

        if (mBrokerObj instanceof Bitmap)
            ((Bitmap) mBrokerObj).recycle();
    }

    public int[] convert(byte buf[])
    {
        int intArr[] = new int[buf.length / 4];
        int offset = 0;
        for (int i = 0; i < intArr.length; i++)
        {
            intArr[i] = (buf[3 + offset] & 0xFF) | ((buf[2 + offset] & 0xFF) << 8) |
                ((buf[1 + offset] & 0xFF) << 16) | ((buf[0 + offset] & 0xFF) << 24);
            offset += 4;
        }
        return intArr;
    }

//            ByteBuffer[] outputBuffers = codec.getOutputBuffers();
//        for (; ; )
//        {
//            int outputBufferId = codec.dequeueOutputBuffer(mBufferInfo, mTimeoutUsec);
//            if (outputBufferId >= 0)
//            {
//                ByteBuffer data = outputBuffers[outputBufferId];
//                data.position(mBufferInfo.offset);
//                data.limit(mBufferInfo.offset + mBufferInfo.size);
//
//                encodeProceed(data, mBufferInfo);
//
//                codec.releaseOutputBuffer(outputBufferId, false);
//            }
//            else if (outputBufferId == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED)
//            {
//                outputBuffers = codec.getOutputBuffers();
//            }
//            else if (outputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED)
//            {
//                MediaFormat format = codec.getOutputFormat();
//            }
}