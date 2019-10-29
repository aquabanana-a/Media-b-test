package com.dobranos.instories.domain.base.mapper;

public class Nv21Mapper
{
    public static int[] toARGB(byte[] yuv, int width, int height)
    {
        int[] argb = new int[width * height];
        final int frameSize = width * height;
        final int ii = 0;
        final int ij = 0;
        final int di = +1;
        final int dj = +1;
        int a = 0;
        for (int i = 0, ci = ii; i < height; ++i, ci += di)
        {
            for (int j = 0, cj = ij; j < width; ++j, cj += dj)
            {
                int y = (0xff & ((int) yuv[ci * width + cj]));
                int v = (0xff & ((int) yuv[frameSize + (ci >> 1) * width + (cj & ~1) + 0]));
                int u = (0xff & ((int) yuv[frameSize + (ci >> 1) * width + (cj & ~1) + 1]));
                y = y < 16 ? 16 : y;
                int r = (int) (1.164f * (y - 16) + 1.596f * (v - 128));
                int g = (int) (1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                int b = (int) (1.164f * (y - 16) + 2.018f * (u - 128));
                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);
                argb[a++] = 0xff000000 | (r << 16) | (g << 8) | b;
            }
        }
        return argb;
    }

    public static byte[] toYV12(byte[] input, int width, int height)
    {
        final int frameSize = width * height;
        final int qFrameSize = frameSize / 4;
        byte[] output = new byte[frameSize * 3 / 2];
        System.arraycopy(input, 0, output, 0, frameSize); // Y
        for (int i = 0; i < qFrameSize; i++)
        {
            output[frameSize + i + qFrameSize] = input[frameSize + i * 2 + 1]; // Cb (U)
            output[frameSize + i] = input[frameSize + i * 2]; // Cr (V)
        }
        return output;
    }

    // the color transform, @see http://stackoverflow.com/questions/15739684/mediacodec-and-camera-color-space-incorrect
    public static byte[] toNV12(byte[] input, int width, int height)
    {
        final int frameSize = width * height;
        final int qFrameSize = frameSize / 4;
        byte[] output = new byte[frameSize * 3 / 2];
        System.arraycopy(input, 0, output, 0, frameSize); // Y
        for (int i = 0; i < qFrameSize; i++)
        {
            output[frameSize + i * 2] = input[frameSize + i * 2 + 1]; // Cb (U)
            output[frameSize + i * 2 + 1] = input[frameSize + i * 2]; // Cr (V)
        }
        return output;
    }

    public static byte[] toI420(byte[] input, int width, int height)
    {
        final int frameSize = width * height;
        final int qFrameSize = frameSize / 4;
        byte[] output = new byte[frameSize * 3 / 2];
        System.arraycopy(input, 0, output, 0, frameSize); // Y
        for (int i = 0; i < qFrameSize; i++)
        {
            output[frameSize + i] = input[frameSize + i * 2 + 1]; // Cb (U)
            output[frameSize + i + qFrameSize] = input[frameSize + i * 2]; // Cr (V)
        }
        return output;
    }
}
