package com.windjammer.zetascale;

/**
 * Created by king on 17-7-24.
 */
public class ZSNativeThread extends ZSNativeObejct {
    private ZSNativeThread() {}

    // use the ZSThreadState to get thread_state handler back
    public static native int ZSInitPerThreadState(ZSThreadState threadState);

    // here we pass thread_state into jni directly
    public static native int ZSReleasePerThreadState(long thread_state);

    // public static native int ZSGetStats(long)
}
