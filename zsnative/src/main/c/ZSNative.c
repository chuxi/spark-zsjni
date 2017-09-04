#include <jni.h>

#include "include/com_windjammer_zetascale_ZSNative.h"
#include "zs.h"

struct ZS_state *state;

JNIEXPORT jint JNICALL Java_com_windjammer_zetascale_ZSNative_ZSLoadProperties
  (JNIEnv *env, jclass jcls, jstring jpath) {
    const char *filepath = (*env)->GetStringUTFChars(env, jpath, NULL);
    ZS_status_t status = ZSLoadProperties(filepath);
    (*env)->ReleaseStringUTFChars(env, jpath, filepath);

    int resultCode = status;

    return resultCode;
}

JNIEXPORT void JNICALL Java_com_windjammer_zetascale_ZSNative_ZSSetProperty
  (JNIEnv *env, jclass jcls, jstring jprop, jstring jprop_value) {
    const char *prop = (*env)->GetStringUTFChars(env, jprop, NULL);
    const char *prop_value = (*env)->GetStringUTFChars(env, jprop_value, NULL);
    ZSSetProperty(prop, prop_value);

    (*env)->ReleaseStringUTFChars(env, jprop, prop);
    (*env)->ReleaseStringUTFChars(env, jprop_value, prop_value);
}

JNIEXPORT jint JNICALL Java_com_windjammer_zetascale_ZSNative_ZSInit
  (JNIEnv *env, jclass jcls, jobject jstate) {
    jclass jstate_class = (*env)->GetObjectClass(env, jstate);
    jfieldID zsStateId = (*env)->GetFieldID(env, jstate_class, "zsStateHandler", "J");

    ZS_status_t status = ZSInit(&state);
    // set the zsStateId in zs_state
    (*env)->SetLongField(env, jstate, zsStateId, (jlong)status);
    return status;
}

JNIEXPORT jint JNICALL Java_com_windjammer_zetascale_ZSNative_ZSShutdown
  (JNIEnv *env, jclass jcls) {
    return ZSShutdown(state);
}

JNIEXPORT jstring JNICALL Java_com_windjammer_zetascale_ZSNative_ZSGetProperty
  (JNIEnv *env, jclass jcls, jstring jprop, jstring jprop_default) {
    const char *prop = (*env)->GetStringUTFChars(env, jprop, NULL);
    const char *prop_value = NULL;
    if (jprop_default != NULL) {
        prop_value = (*env)->GetStringUTFChars(env, jprop_default, NULL);
    }
    const char *ret = ZSGetProperty(prop, prop_value);
    jstring jret = NULL;
    if (ret != NULL) {
        jret = (*env)->NewStringUTF(env, ret);
        ZSFreeBuffer((char *)ret);
    }

    (*env)->ReleaseStringUTFChars(env, jprop, prop);
    (*env)->ReleaseStringUTFChars(env, jprop_default, prop_value);

    return jret;
}