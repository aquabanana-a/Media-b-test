package com.dobranos.instories.domain.base.mapper;

public class Yv12Mapper
{
    public static byte[] toNV12(byte[] input, int width, int height)
    {
        final int frameSize = width * height;
        final int qFrameSize = frameSize / 4;
        byte[] output = new byte[frameSize * 3 / 2];
        System.arraycopy(input, 0, output, 0, frameSize); // Y
        for (int i = 0; i < qFrameSize; i++)
        {
            output[frameSize + i * 2] = input[frameSize + i + qFrameSize]; // Cb (U)
            output[frameSize + i * 2 + 1] = input[frameSize + i]; // Cr (V)
        }
        return output;
    }

    public static byte[] toI420(byte[] input, int width, int height)
    {
        final int frameSize = width * height;
        final int qFrameSize = frameSize / 4;
        byte[] output = new byte[frameSize * 3 / 2];
        System.arraycopy(input, 0, output, 0, frameSize); // Y
        System.arraycopy(input, frameSize + qFrameSize, output, frameSize, qFrameSize); // Cb (U)
        System.arraycopy(input, frameSize, output, frameSize + qFrameSize, qFrameSize); // Cr (V)
        return output;
    }

    public static byte[] toNV21(byte[] input, int width, int height)
    {
        final int frameSize = width * height;
        final int qFrameSize = frameSize / 4;
        byte[] output = new byte[frameSize * 3 / 2];
        System.arraycopy(input, 0, output, 0, frameSize); // Y
        for (int i = 0; i < qFrameSize; i++)
        {
            output[frameSize + i * 2 + 1] = input[frameSize + i + qFrameSize]; // Cb (U)
            output[frameSize + i * 2] = input[frameSize + i]; // Cr (V)
        }
        return output;
    }
}
