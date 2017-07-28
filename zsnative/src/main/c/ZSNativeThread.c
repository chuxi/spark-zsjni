//
// Created by king on 17-7-26.
//
#include "include/com_windjammer_zetascale_ZSNativeThread.h"
#include "zs.h"

extern struct ZS_state *state;

JNIEXPORT jint JNICALL Java_com_windjammer_zetascale_ZSNativeThread_ZSInitPerThreadState
        (JNIEnv *env, jclass jcls, jobject jthreadState) {
    jclass jthreadStateClass = (*env)->GetObjectClass(env, jthreadState);
    jfieldID thd_stateId = (*env)->GetFieldID(env, jthreadStateClass, "thread_state", "J");

    struct ZS_thread_state *thd_state;
    ZS_status_t status = ZSInitPerThreadState(state, &thd_state);

    // store thd_state in jthreadState object
    (*env)->SetLongField(env, jthreadState, thd_stateId, (uint64_t)thd_state);
    return status;
}

/*
 * Class:     com_windjammer_zetascale_ZSNativeThread
 * Method:    ZSReleasePerThreadState
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_windjammer_zetascale_ZSNativeThread_ZSReleasePerThreadState
        (JNIEnv *env, jclass jcls, jlong jthread_state) {
    struct ZS_thread_state *thd_state = (struct ZS_thread_state *)jthread_state;
    ZS_status_t status = ZSReleasePerThreadState(&thd_state);
    return status;
}