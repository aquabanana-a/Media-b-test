//
// Created by Andromeda on 28.10.2019.
//

#include <jni.h>
#include <iostream>
#include <cstdint>

extern "C"
{
    JNIEXPORT jbyteArray JNICALL Java_com_dobranos_instories_domain_base_mapper_YuvMapper_ARGBtoYUV420SemiPlanarNative(JNIEnv *env, jobject *thiz, jintArray i, jint width, jint height)
    {
        int *input = env->GetIntArrayElements(i, NULL);
        int frameSize = width * height;

        int yuv420spSize = width * height * 3 / 2;
        uint8_t *yuv420sp = new uint8_t[yuv420spSize];

        int yIndex = 0;
        int uvIndex = frameSize;

        int a, R, G, B, Y, U, V;
        int index = 0;

        for (int j = 0; j < height; j++)
        {
            for (int i = 0; i < width; i++)
            {
                a = (input[index] & 0xff000000) >> 24; // a is not used obviously
                R = (input[index] & 0xff0000) >> 16;
                G = (input[index] & 0xff00) >> 8;
                B = (input[index] & 0xff) >> 0;

                // well known RGB to YUV algorithm
                Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
                U = ((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128;
                V = ((112 * R - 94 * G - 18 * B + 128) >> 8) + 128;


                // NV21 has a plane of Y and interleaved planes of VU each sampled by a factor of 2
                //    meaning for every 4 Y pixels there are 1 V and 1 U.  Note the sampling is every other
                //    pixel AND every other scanline.
                yuv420sp[yIndex++] = (uint8_t)((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));
                if (j % 2 == 0 && index % 2 == 0) {
                    yuv420sp[uvIndex++] = (uint8_t)((V < 0) ? 0 : ((V > 255) ? 255 : V));
                    yuv420sp[uvIndex++] = (uint8_t)((U < 0) ? 0 : ((U > 255) ? 255 : U));
                }

                index++;
            }
        }

//        jbyteArray ret = env->NewByteArray(yuv420spSize);
//        memcpy(ret, yuv420sp, yuv420spSize);
//        env->SetByteArrayRegion(ret, 0, yuv420spSize, yuv420sp);

        jbyteArray ret = env->NewByteArray(yuv420spSize);
        env->SetByteArrayRegion(ret, 0, yuv420spSize, reinterpret_cast<const jbyte *>(yuv420sp));

        return ret;
    }

}