//
// Created by king on 17-7-26.
//

#include "include/com_windjammer_zetascale_ZSNativeUtils.h"
#include "zs.h"


JNIEXPORT jstring JNICALL Java_com_windjammer_zetascale_ZSNativeUtils_ZSErrorString
        (JNIEnv *env, jclass jcls, jint jcode) {
    char *message = ZSStrError((ZS_status_t)jcode);
    return (*env)->NewStringUTF(env, message);
}