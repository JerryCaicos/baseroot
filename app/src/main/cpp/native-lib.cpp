//
// Created by JerryCaicos on 2017/12/13.
//
#include <jni.h>

extern "C"
JNIEXPORT jint JNICALL
Java_com_base_application_baseapplication_MainActivity_maxFromJNI(JNIEnv *env, jobject instance,
                                                                  jint a, jint b) {

    // TODO
    return a > b ? a : b;
}
