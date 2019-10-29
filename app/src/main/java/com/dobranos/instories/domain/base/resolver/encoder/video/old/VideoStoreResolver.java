package com.dobranos.instories.domain.base.resolver.encoder.video.old;

import android.media.MediaCodec;
import android.util.Log;

import com.dobranos.instories.domain.base.model.encoder.EncodeBroker;
import com.dobranos.instories.domain.base.model.encoder.video.VideoEncodeListener;

import org.jcodec.codecs.h264.H264Utils;
import org.jcodec.common.Codec;
import org.jcodec.common.VideoCodecMeta;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Packet;
import org.jcodec.containers.mp4.Brand;
import org.jcodec.containers.mp4.MP4Packet;
import org.jcodec.containers.mp4.muxer.MP4Muxer;
import org.jcodec.containers.mp4.muxer.MP4MuxerTrack;

import java.io.File;
import java.nio.ByteBuffer;

public class VideoStoreResolver implements VideoEncodeListener
{
    private File videoFile;
    private VideoEncodeResolver encoder;

    private MP4Muxer muxer;
    private MP4MuxerTrack track;
    private SeekableByteChannel ch;
    private int frameNo;

    public VideoStoreResolver(VideoEncodeResolver encoder, File file)
    {
        this.encoder = encoder;
        this.encoder.addListener(this);
        this.videoFile = file;

        try
        {
            videoFile.createNewFile();
        }
        catch (Exception e) { }
    }

    public VideoStoreResolver(VideoEncodeResolver encoder, File rootFolder, String fileName)
    {
        this(encoder, new File(rootFolder, fileName + ".mp4"));
    }

    public void onPrepare(EncodeBroker broker)
    {
        try
        {
            if (muxer == null)
                muxer = MP4Muxer.createMP4Muxer(ch = NIOUtils.writableChannel(videoFile), Brand.MP4);

            if (track == null)
                track = (MP4MuxerTrack) muxer.addVideoTrack(Codec.H264, VideoCodecMeta.createSimpleVideoCodecMeta(encoder.getFrameSize(), ColorSpace.SAME));

            Log.d("my", "track.onPrepare");
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public void onRelease(EncodeBroker broker)
    {
        try
        {
            if (muxer != null)
            {
                muxer.finish();
                muxer = null;
            }

//            if (ch != null)
//            {
//                ch.close();
//                ch = null;
//            }

            Log.d("my", "track.onRelease");
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public void onFrame(ByteBuffer data, MediaCodec.BufferInfo info)
    {
        try
        {
            //            new MP4Packet(
//                result,      // Bytebuffer that contains encoded frame

//                i,             // Presentation timestamp ( think seconds ) expressed in timescale units ( just multiply second by
//                // timescale value below. This is to avoid floats.
//                // Example: timescale = 25, pts = 0, 1, 2, 3, 4, 5 .... ( PAL 25 fps )
//                // Example: timescale = 30000, pts = 1001, 2002, 3003, 4004, 5005, 6006, 7007 ( NTSC 29.97 fps )

//                timescale, // See above

//                1,            // Duration of a frame in timescale units ( think seconds multiplied by number above)
//                // Examlle: timescale = 25, duration = 1 ( PAL 25 fps )
//                // Example: timescale = 30000, duration = 1001 ( NTSC 29.97 fps )

//                frameNo,  // Just a number of frame, doesn't have anything to do with timing

//                true,         // Is it an I-frame, i.e. is this a seek point? Players use this information to instantly know where to seek

//                null,         // just ignore, should be null. This is used by the older brother of MP4 - Apple Quicktime which supports
//                // tape timecode
//                i,             // just put the same as pts above
//                0             // sample entry, should be 0
//            )

            // todo:
            // normalize framerate to timesacale

            synchronized (syncLo)
            {
                track.addFrame(MP4Packet.createPacket(data, frameNo, 30, 1, frameNo++, H264Utils.isByteBufferIDRSlice(data) ? Packet.FrameType.KEY : Packet.FrameType.INTER, null));
            }

        }
        catch (Exception e) { e.printStackTrace(); }
    }

    private Object syncLo = new Object();
}